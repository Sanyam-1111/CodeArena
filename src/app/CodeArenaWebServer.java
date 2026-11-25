package app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import contest.Contest;
import contest.ContestManager;
import contest.ContestRegistration;
import contest.ContestRegistrationManager;
import contest.Problem;
import contest.ProblemManager;
import leaderboard.LeaderboardService;
import models.LeaderboardEntry;
import models.EvaluationResult;
import models.PlagiarismReport;
import models.SimilarityMatch;
import models.Student;
import models.Submission;
import models.TestCase;
import plagiarism.PlagiarismService;
import services.EvaluationService;
import services.SubmissionService;
import student.StudentService;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class CodeArenaWebServer {

    private final StudentService studentService = new StudentService();
    private final ContestManager contestManager = new ContestManager();
    private final ProblemManager problemManager = new ProblemManager(contestManager);
    private final ContestRegistrationManager registrationManager =
            new ContestRegistrationManager(contestManager, studentService);
    private final EvaluationService evaluationService = new EvaluationService();
    private final SubmissionService submissionService = new SubmissionService(
            studentService, contestManager, problemManager, evaluationService);
    private final LeaderboardService leaderboardService = new LeaderboardService();
    private final PlagiarismService plagiarismService = new PlagiarismService();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static void main(String[] args) throws Exception {
        CodeArenaWebServer application = new CodeArenaWebServer();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 8080), 0);
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.createContext("/", application::handleRequest);
        server.start();

        String address = "http://127.0.0.1:8080";
        System.out.println("CodeArena is running at " + address);
        System.out.println("Keep this window open while using the application.");
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(address));
            } catch (Exception e) {
                System.out.println("Open " + address + " in your browser.");
            }
        }
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            if (path.startsWith("/api/")) {
                handleApi(exchange, path);
            } else {
                handleStatic(exchange, path);
            }
        } catch (IllegalArgumentException e) {
            sendJson(exchange, 400, object("error", e.getMessage()));
        } catch (Exception e) {
            sendJson(exchange, 500, object("error", "The request could not be completed."));
        }
    }

    private void handleApi(HttpExchange exchange, String path) throws IOException {
        String method = exchange.getRequestMethod();
        Map<String, String> params = method.equals("GET")
                ? parseForm(exchange.getRequestURI().getRawQuery())
                : method.equals("POST") ? parseForm(readBody(exchange)) : new LinkedHashMap<>();

        if (path.equals("/api/students") && method.equals("GET")) {
            List<Object> values = new ArrayList<>();
            for (Student student : studentService.getAllStudents()) {
                values.add(studentJson(student));
            }
            sendJson(exchange, 200, values);
            return;
        }
        if (path.equals("/api/students") && method.equals("POST")) {
            boolean created = studentService.registerStudent(
                    integer(params, "studentId"), required(params, "name"), required(params, "email"));
            if (!created) {
                throw new IllegalArgumentException("Student could not be registered.");
            }
            sendJson(exchange, 201, studentJson(studentService.getStudentById(integer(params, "studentId"))));
            return;
        }
        if (path.equals("/api/students/update") && method.equals("POST")) {
            int studentId = integer(params, "studentId");
            if (!studentService.updateStudent(studentId, required(params, "name"), required(params, "email"))) {
                throw new IllegalArgumentException("Student could not be updated.");
            }
            sendJson(exchange, 200, studentJson(studentService.getStudentById(studentId)));
            return;
        }
        if (path.equals("/api/students/delete") && method.equals("POST")) {
            if (!studentService.deleteStudent(integer(params, "studentId"))) {
                throw new IllegalArgumentException("Student not found.");
            }
            sendJson(exchange, 200, object("deleted", true));
            return;
        }
        if (path.equals("/api/contests") && method.equals("GET")) {
            List<Object> values = new ArrayList<>();
            for (Contest contest : contestManager.getAllContests()) {
                values.add(contestJson(contest));
            }
            sendJson(exchange, 200, values);
            return;
        }
        if (path.equals("/api/contests") && method.equals("POST")) {
            LocalDateTime start = dateTime(params, "startTime");
            LocalDateTime end = dateTime(params, "endTime");
            Contest contest = contestManager.addContest(
                    required(params, "name"), required(params, "description"), start, end);
            sendJson(exchange, 201, contestJson(contest));
            return;
        }
        if (path.equals("/api/contests/update") && method.equals("POST")) {
            int contestId = integer(params, "contestId");
            LocalDateTime start = dateTime(params, "startTime");
            LocalDateTime end = dateTime(params, "endTime");
            if (!contestManager.updateContest(contestId, required(params, "name"),
                    required(params, "description"), start, end)) {
                throw new IllegalArgumentException("Contest could not be updated.");
            }
            sendJson(exchange, 200, contestJson(contestManager.getContestById(contestId)));
            return;
        }
        if (path.equals("/api/contests/delete") && method.equals("POST")) {
            if (!contestManager.deleteContest(integer(params, "contestId"))) {
                throw new IllegalArgumentException("Contest not found.");
            }
            sendJson(exchange, 200, object("deleted", true));
            return;
        }
        if (path.equals("/api/problems") && method.equals("GET")) {
            int contestId = params.containsKey("contestId") ? integer(params, "contestId") : 0;
            List<Object> values = new ArrayList<>();
            for (Problem problem : problemManager.getAllProblems()) {
                if (contestId == 0 || problem.getContestId() == contestId) {
                    values.add(problemJson(problem));
                }
            }
            sendJson(exchange, 200, values);
            return;
        }
        if (path.equals("/api/problems") && method.equals("POST")) {
            Problem problem = problemManager.addProblem(
                    integer(params, "contestId"), required(params, "title"),
                    required(params, "description"), required(params, "difficulty"),
                    integer(params, "maxScore"), value(params, "sampleInput", ""),
                    value(params, "sampleOutput", ""));
            sendJson(exchange, 201, problemJson(problem));
            return;
        }
        if (path.equals("/api/problems/update") && method.equals("POST")) {
            int problemId = integer(params, "problemId");
            boolean updated = problemManager.updateProblem(problemId, required(params, "title"),
                    required(params, "description"), required(params, "difficulty"),
                    integer(params, "maxScore"), value(params, "sampleInput", ""),
                    value(params, "sampleOutput", ""));
            if (!updated) {
                throw new IllegalArgumentException("Problem could not be updated.");
            }
            sendJson(exchange, 200, problemJson(problemManager.getProblemById(problemId)));
            return;
        }
        if (path.equals("/api/problems/delete") && method.equals("POST")) {
            if (!problemManager.deleteProblem(integer(params, "problemId"))) {
                throw new IllegalArgumentException("Problem not found.");
            }
            sendJson(exchange, 200, object("deleted", true));
            return;
        }
        if (path.equals("/api/testcases") && method.equals("GET")) {
            List<Object> values = new ArrayList<>();
            for (TestCase testCase : problemManager.getTestCasesForProblem(integer(params, "problemId"))) {
                values.add(testCaseJson(testCase));
            }
            sendJson(exchange, 200, values);
            return;
        }
        if (path.equals("/api/testcases") && method.equals("POST")) {
            TestCase testCase = problemManager.addTestCase(
                    integer(params, "problemId"), value(params, "input", ""),
                    value(params, "expectedOutput", ""), decimal(params, "marks"));
            sendJson(exchange, 201, testCaseJson(testCase));
            return;
        }
        if (path.equals("/api/registrations") && method.equals("GET")) {
            List<Object> values = new ArrayList<>();
            for (ContestRegistration registration : registrationManager.getAllRegistrations()) {
                values.add(registrationJson(registration));
            }
            sendJson(exchange, 200, values);
            return;
        }
        if (path.equals("/api/registrations") && method.equals("POST")) {
            int contestId = integer(params, "contestId");
            int studentId = integer(params, "studentId");
            for (ContestRegistration registration : registrationManager.getAllRegistrations()) {
                if (registration.getContestId() == contestId
                        && registration.getStudentId() == studentId
                        && registration.getRegistrationStatus().equalsIgnoreCase("Registered")) {
                    throw new IllegalArgumentException("Student is already registered for this contest.");
                }
            }
            registrationManager.registerStudent(contestId, studentId);
            for (ContestRegistration registration : registrationManager.getAllRegistrations()) {
                if (registration.getContestId() == contestId
                        && registration.getStudentId() == studentId
                        && registration.getRegistrationStatus().equalsIgnoreCase("Registered")) {
                    sendJson(exchange, 201, registrationJson(registration));
                    return;
                }
            }
            throw new IllegalArgumentException("Registration could not be completed.");
        }
        if (path.equals("/api/registrations/cancel") && method.equals("POST")) {
            int contestId = integer(params, "contestId");
            int studentId = integer(params, "studentId");
            boolean wasRegistered = false;
            for (ContestRegistration registration : registrationManager.getAllRegistrations()) {
                if (registration.getContestId() == contestId && registration.getStudentId() == studentId
                        && registration.getRegistrationStatus().equalsIgnoreCase("Registered")) {
                    wasRegistered = true;
                    break;
                }
            }
            if (!wasRegistered) {
                throw new IllegalArgumentException("Active registration not found.");
            }
            registrationManager.cancelRegistration(contestId, studentId);
            sendJson(exchange, 200, object("cancelled", true));
            return;
        }
        if (path.equals("/api/submissions") && method.equals("GET")) {
            List<Object> values = new ArrayList<>();
            for (Submission submission : submissionService.getAllSubmissions()) {
                values.add(submissionJson(submission));
            }
            sendJson(exchange, 200, values);
            return;
        }
        if (path.equals("/api/submissions") && method.equals("POST")) {
            Submission submission = submissionService.createSubmission(
                    integer(params, "studentId"), integer(params, "contestId"),
                    integer(params, "problemId"), required(params, "sourceCode"));
            sendJson(exchange, 201, submissionJson(submission));
            return;
        }
        if (path.equals("/api/evaluate") && method.equals("POST")) {
            int submissionId = integer(params, "submissionId");
            Submission submission = submissionService.getSubmissionById(submissionId);
            if (submission == null) {
                throw new IllegalArgumentException("Submission not found.");
            }
            ArrayList<TestCase> testCases = problemManager.getTestCasesForProblem(submission.getProblemId());
            ArrayList<String> outputs = new ArrayList<>();
            for (int i = 0; i < testCases.size(); i++) {
                outputs.add(value(params, "output" + i, ""));
            }
            EvaluationResult result = submissionService.evaluateSubmission(submissionId, outputs);
            sendJson(exchange, 200, evaluationJson(result));
            return;
        }
        if (path.equals("/api/leaderboard") && method.equals("GET")) {
            int contestId = integer(params, "contestId");
            leaderboardService.refreshContestLeaderboard(
                    contestId, submissionService.getAllSubmissions(), studentService);
            List<Object> values = new ArrayList<>();
            for (LeaderboardEntry entry : leaderboardService.generateLeaderboard(contestId)) {
                values.add(object(
                        "contestId", entry.getContestId(),
                        "studentId", entry.getStudent().getStudentId(),
                        "studentName", entry.getStudent().getName(),
                        "score", entry.getScore(),
                        "evaluationTimeMs", entry.getEvaluationTimeMs(),
                        "submissionTime", entry.getSubmissionTime().toString()));
            }
            sendJson(exchange, 200, values);
            return;
        }
        if (path.equals("/api/plagiarism") && method.equals("GET")) {
            int contestId = integer(params, "contestId");
            int problemId = integer(params, "problemId");
            double threshold = decimal(params, "threshold");
            if (Double.isNaN(threshold) || Double.isInfinite(threshold)) {
                throw new IllegalArgumentException("Threshold must be between 0 and 100.");
            }
            PlagiarismReport report = plagiarismService.analyzeProblemSubmissions(
                    submissionService.getAllSubmissions(), contestId, problemId, threshold);
            List<Object> matches = new ArrayList<>();
            for (SimilarityMatch match : report.getMatches()) {
                matches.add(object(
                        "submissionIdA", match.getSubmissionIdA(),
                        "submissionIdB", match.getSubmissionIdB(),
                        "similarity", match.getSimilarityPercentage(),
                        "reviewRecommended", match.isReviewRecommended()));
            }
            sendJson(exchange, 200, object(
                    "totalComparisons", report.getTotalComparisons(),
                    "suspiciousMatches", report.getSuspiciousMatches(),
                    "matches", matches));
            return;
        }

        sendJson(exchange, 404, object("error", "API route not found."));
    }

    private void handleStatic(HttpExchange exchange, String path) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            sendJson(exchange, 405, object("error", "Method not allowed."));
            return;
        }
        String filename;
        String contentType;
        if (path.equals("/") || path.equals("/index.html")) {
            filename = "index.html";
            contentType = "text/html; charset=utf-8";
        } else if (path.equals("/styles.css")) {
            filename = "styles.css";
            contentType = "text/css; charset=utf-8";
        } else if (path.equals("/app.js")) {
            filename = "app.js";
            contentType = "text/javascript; charset=utf-8";
        } else {
            sendJson(exchange, 404, object("error", "Page not found."));
            return;
        }

        File file = new File("frontend", filename);
        if (!file.isFile()) {
            sendJson(exchange, 500, object("error", "Frontend files were not found. Start the app from the project folder."));
            return;
        }
        byte[] content = Files.readAllBytes(file.toPath());
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(200, content.length);
        exchange.getResponseBody().write(content);
        exchange.close();
    }

    private Map<String, Object> studentJson(Student student) {
        return object("studentId", student.getStudentId(), "name", student.getName(), "email", student.getEmail());
    }

    private Map<String, Object> contestJson(Contest contest) {
        return object("contestId", contest.getContestId(), "name", contest.getContestName(),
                "description", contest.getDescription(), "startTime", contest.getStartTime().toString(),
                "endTime", contest.getEndTime().toString(), "status", contest.getStatus());
    }

    private Map<String, Object> problemJson(Problem problem) {
        return object("problemId", problem.getProblemId(), "contestId", problem.getContestId(),
                "title", problem.getProblemTitle(), "description", problem.getDescription(),
                "difficulty", problem.getDifficulty(), "maxScore", problem.getMaxScore(),
                "sampleInput", problem.getSampleInput(), "sampleOutput", problem.getSampleOutput());
    }

    private Map<String, Object> testCaseJson(TestCase testCase) {
        return object("testCaseId", testCase.getTestCaseId(), "input", testCase.getInput(),
                "expectedOutput", testCase.getExpectedOutput(), "marks", testCase.getMarks());
    }

    private Map<String, Object> registrationJson(ContestRegistration registration) {
        return object("registrationId", registration.getRegistrationId(),
                "contestId", registration.getContestId(), "studentId", registration.getStudentId(),
                "registrationDate", registration.getRegistrationDate().toString(),
                "status", registration.getRegistrationStatus());
    }

    private Map<String, Object> submissionJson(Submission submission) {
        EvaluationResult result = submission.getEvaluationResult();
        return object("submissionId", submission.getSubmissionId(), "studentId", submission.getStudentId(),
                "contestId", submission.getContestId(), "problemId", submission.getProblemId(),
                "submissionTime", submission.getSubmissionTime().toString(),
                "sourceCode", submission.getSourceCode(),
                "status", result == null ? "Not evaluated" : result.getStatus(),
                "score", result == null ? null : result.getScore());
    }

    private Map<String, Object> evaluationJson(EvaluationResult result) {
        return object("totalTestCases", result.getTotalTestCases(),
                "passedTestCases", result.getPassedTestCases(),
                "failedTestCases", result.getFailedTestCases(),
                "score", result.getScore(), "status", result.getStatus(),
                "comparisonTimeNanos", result.getEvaluationTime());
    }

    private Map<String, Object> object(Object... pairs) {
        Map<String, Object> value = new LinkedHashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            value.put((String) pairs[i], pairs[i + 1]);
        }
        return value;
    }

    private String toJson(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return quote((String) value);
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof Map) {
            StringBuilder json = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                if (!first) {
                    json.append(',');
                }
                json.append(quote(String.valueOf(entry.getKey()))).append(':').append(toJson(entry.getValue()));
                first = false;
            }
            return json.append('}').toString();
        }
        if (value instanceof Iterable) {
            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            for (Object item : (Iterable<?>) value) {
                if (!first) {
                    json.append(',');
                }
                json.append(toJson(item));
                first = false;
            }
            return json.append(']').toString();
        }
        return quote(value.toString());
    }

    private String quote(String value) {
        StringBuilder result = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            switch (character) {
                case '"': result.append("\\\""); break;
                case '\\': result.append("\\\\"); break;
                case '\b': result.append("\\b"); break;
                case '\f': result.append("\\f"); break;
                case '\n': result.append("\\n"); break;
                case '\r': result.append("\\r"); break;
                case '\t': result.append("\\t"); break;
                default:
                    if (character < 0x20) {
                        result.append(String.format("\\u%04x", (int) character));
                    } else {
                        result.append(character);
                    }
            }
        }
        return result.append('"').toString();
    }

    private void sendJson(HttpExchange exchange, int status, Object value) throws IOException {
        byte[] content = toJson(value).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, content.length);
        exchange.getResponseBody().write(content);
        exchange.close();
    }

    private String readBody(HttpExchange exchange) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int size;
        while ((size = exchange.getRequestBody().read(chunk)) != -1) {
            buffer.write(chunk, 0, size);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    private Map<String, String> parseForm(String body) throws IOException {
        Map<String, String> values = new LinkedHashMap<>();
        if (body == null || body.isEmpty()) {
            return values;
        }
        for (String pair : body.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], "UTF-8");
            String value = parts.length > 1 ? URLDecoder.decode(parts[1], "UTF-8") : "";
            values.put(key, value);
        }
        return values;
    }

    private String required(Map<String, String> values, String key) {
        String value = values.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(key + " is required.");
        }
        return value;
    }

    private String value(Map<String, String> values, String key, String fallback) {
        return values.containsKey(key) ? values.get(key) : fallback;
    }

    private int integer(Map<String, String> values, String key) {
        try {
            return Integer.parseInt(required(values, key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " must be a whole number.");
        }
    }

    private double decimal(Map<String, String> values, String key) {
        try {
            return Double.parseDouble(required(values, key));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " must be a number.");
        }
    }

    private LocalDateTime dateTime(Map<String, String> values, String key) {
        try {
            return LocalDateTime.parse(required(values, key), dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(key + " must use yyyy-MM-ddTHH:mm.");
        }
    }
}
