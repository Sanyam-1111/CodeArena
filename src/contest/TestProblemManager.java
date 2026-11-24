
package contest;

public class TestProblemManager {

    public static void main(String[] args) {

        ProblemManager manager = new ProblemManager();

        // 1. Add Problems
        System.out.println("\n--- ADDING PROBLEMS ---");

        manager.addProblem(
            101,
            "Two Sum",
            "Find two numbers whose sum equals target.",
            "Easy",
            100,
            "4 2 7 11 15 9",
            "2 7"
        );

        manager.addProblem(
            101,
            "Binary Search",
            "Find the target element in a sorted array.",
            "Medium",
            200,
            "5 1 2 3 4 5 3",
            "2"
        );

        // 2. View All Problems
        System.out.println("\n--- VIEW ALL PROBLEMS ---");
        manager.viewAllProblems();

        // 3. Search Problem
        System.out.println("\n--- SEARCH PROBLEM ---");

        Problem p = manager.searchProblemById(1);

        if (p != null) {
            p.displayProblem();
        } else {
            System.out.println("Problem not found!");
        }

        // 4. Update Problem
        System.out.println("\n--- UPDATE PROBLEM ---");

        manager.updateProblem(
            1,
            "Two Sum Updated",
            "Find two elements whose sum equals target.",
            "Medium",
            150,
            "3 2 7 9",
            "2 7"
        );

        // 5. View Problems By Contest ID
        System.out.println("\n--- PROBLEMS BY CONTEST ---");
        manager.viewProblemsByContestId(101);

        // 6. Delete Problem
        System.out.println("\n--- DELETE PROBLEM ---");
        manager.deleteProblem(2);

        // 7. View After Deletion
        System.out.println("\n--- FINAL PROBLEM LIST ---");
        manager.viewAllProblems();
    }
}

