const state={students:[],contests:[],problems:[],registrations:[],submissions:[],testCases:[]};
const $=selector=>document.querySelector(selector);
const $$=selector=>Array.from(document.querySelectorAll(selector));
let toastTimer;

async function api(path,values){
  const options=values?{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8'},body:new URLSearchParams(values)}:{};
  const response=await fetch(path,options);
  let payload={};
  try{payload=await response.json()}catch(e){payload={error:'The server returned an invalid response.'}}
  if(!response.ok)throw new Error(payload.error||'Request failed.');
  return payload;
}

function escapeHtml(value){
  return String(value==null?'':value).replace(/[&<>"']/g,char=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[char]));
}

function notify(message,isError=false){
  const toast=$('#toast');
  toast.textContent=message;
  toast.classList.toggle('error',isError);
  toast.classList.add('show');
  clearTimeout(toastTimer);
  toastTimer=setTimeout(()=>toast.classList.remove('show'),3200);
}

function setOptions(select,items,idField,labelField,placeholder,includeAll=false){
  if(!select)return;
  const prior=select.value;
  let html=includeAll?'<option value="all">All contests</option>':'';
  if(placeholder)html+=`<option value="">${escapeHtml(placeholder)}</option>`;
  html+=items.map(item=>`<option value="${escapeHtml(item[idField])}">${escapeHtml(item[labelField])}</option>`).join('');
  select.innerHTML=html;
  if(Array.from(select.options).some(option=>option.value===prior))select.value=prior;
  else if(includeAll)select.value='all';
  else if(items.length)select.value=String(items[0][idField]);
}

async function loadAll(){
  const [students,contests,problems,registrations,submissions]=await Promise.all([
    api('/api/students'),api('/api/contests'),api('/api/problems'),api('/api/registrations'),api('/api/submissions')
  ]);
  Object.assign(state,{students,contests,problems,registrations,submissions});
  updateSelectors();
  renderOverview();
  renderStudents();
  renderContests();
  renderProblems();
  renderRegistrations();
  renderSubmissions();
  renderEvaluationCases();
}

function updateSelectors(){
  const contests=state.contests.map(item=>({contestId:item.contestId,label:`${item.name} · #${item.contestId}`}));
  const students=state.students.map(item=>({studentId:item.studentId,label:`${item.name} · #${item.studentId}`}));
  const problems=state.problems.map(item=>({problemId:item.problemId,label:`${item.title} · #${item.problemId}`}));
  setOptions($('#problem-contest'),contests,'contestId','label','Choose a contest');
  setOptions($('#problem-filter'),contests,'contestId','label','Choose a contest');
  setOptions($('#registration-contest'),contests,'contestId','label','Choose a contest');
  setOptions($('#registration-filter'),contests,'contestId','label',null,true);
  setOptions($('#registration-student'),students,'studentId','label','Choose a student');
  setOptions($('#submission-student'),students,'studentId','label','Choose a student');
  setOptions($('#submission-contest'),contests,'contestId','label','Choose a contest');
  setOptions($('#leaderboard-contest'),contests,'contestId','label','Choose a contest');
  setOptions($('#plagiarism-contest'),contests,'contestId','label','Choose a contest');
  updateProblemOptions($('#testcase-problem'),state.problems,'Choose a problem');
  updateProblemOptions($('#testcase-filter'),state.problems,'Choose a problem');
  updateProblemOptions($('#submission-problem'),state.problems,'Choose a problem');
  updateProblemOptions($('#plagiarism-problem'),state.problems,'Choose a problem');
  setOptions($('#evaluation-submission'),state.submissions.map(item=>({submissionId:item.submissionId,label:`#${item.submissionId} · ${item.status}`})),'submissionId','label','Choose a submission');
}

function updateProblemOptions(select,problems,placeholder){
  const contestSelect=select===$('#submission-problem')?$('#submission-contest'):
    select===$('#plagiarism-problem')?$('#plagiarism-contest'):
    select===$('#testcase-problem')?$('#problem-filter'):null;
  let available=problems;
  if(contestSelect&&contestSelect.value&&contestSelect.value!=='all')available=problems.filter(item=>String(item.contestId)===contestSelect.value);
  const options=available.map(item=>({problemId:item.problemId,label:`${item.title} · #${item.problemId}`}));
  setOptions(select,options,'problemId','label',placeholder);
}

function renderOverview(){
  $('#stat-students').textContent=state.students.length;
  $('#stat-contests').textContent=state.contests.length;
  $('#stat-problems').textContent=state.problems.length;
  $('#stat-submissions').textContent=state.submissions.length;
  const recent=state.submissions.slice().reverse().slice(0,4);
  $('#recent-submissions').innerHTML=recent.length?recent.map(item=>`<div class="recent-row"><span class="recent-id">#${item.submissionId}</span><span><b>${escapeHtml(studentName(item.studentId))}</b><small>${escapeHtml(contestName(item.contestId))} · ${escapeHtml(problemName(item.problemId))}</small></span><span class="status-pill ${item.status==='Accepted'?'upcoming':''}">${escapeHtml(item.status)}</span></div>`).join(''):'No submissions yet. They’ll show here when students submit.';
}

function renderStudents(){
  $('#students-count').textContent=state.students.length;
  $('#students-empty').classList.toggle('hidden',state.students.length>0);
  $('#students-table').innerHTML=state.students.map(student=>`<tr><td><span class="mono">${student.studentId}</span></td><td><strong>${escapeHtml(student.name)}</strong></td><td>${escapeHtml(student.email)}</td><td class="action-cell"><button class="table-action" data-edit-student="${student.studentId}">Edit</button><button class="table-action danger" data-delete-student="${student.studentId}">Remove</button></td></tr>`).join('');
}

function renderContests(){
  $('#contests-count').textContent=state.contests.length;
  $('#contests-empty').classList.toggle('hidden',state.contests.length>0);
  $('#contest-cards').innerHTML=state.contests.map(contest=>`<article class="contest-card"><div class="contest-card-top"><div><h3>${escapeHtml(contest.name)}</h3><p>${escapeHtml(contest.description)}</p></div><span class="status-pill ${contest.status.toLowerCase()}">${escapeHtml(contest.status)}</span></div><div class="contest-meta"><span>CONTEST #${contest.contestId}</span><span>${escapeHtml(formatDate(contest.startTime))}</span><span>${escapeHtml(formatDate(contest.endTime))}</span><span class="card-actions"><button class="table-action" data-edit-contest="${contest.contestId}">Edit</button><button class="table-action danger" data-delete-contest="${contest.contestId}">Remove</button></span></div></article>`).join('');
}

function renderProblems(){
  const filter=$('#problem-filter').value;
  const problems=state.problems.filter(problem=>!filter||filter==='all'||String(problem.contestId)===filter);
  $('#problems-empty').classList.toggle('hidden',problems.length>0);
  $('#problem-cards').innerHTML=problems.map(problem=>`<article class="problem-card"><div class="problem-card-top"><span class="mono">PROBLEM #${problem.problemId}</span><span class="difficulty-pill ${problem.difficulty.toLowerCase()}">${escapeHtml(problem.difficulty)}</span></div><h3>${escapeHtml(problem.title)}</h3><p>${escapeHtml(problem.description)}</p><div class="problem-meta">CONTEST #${problem.contestId} · ${problem.maxScore} MAX MARKS</div><div class="card-actions"><button class="table-action" data-edit-problem="${problem.problemId}">Edit</button><button class="table-action danger" data-delete-problem="${problem.problemId}">Remove</button></div></article>`).join('');
  const selected=$('#testcase-filter').value;
  if(!selected&&problems.length)$('#testcase-filter').value=String(problems[0].problemId);
  renderTestCases();
}

async function renderTestCases(){
  const problemId=$('#testcase-filter').value;
  if(!problemId){state.testCases=[];$('#testcases-table').innerHTML='';$('#testcases-empty').classList.remove('hidden');return;}
  try{
    state.testCases=await api(`/api/testcases?problemId=${encodeURIComponent(problemId)}`);
    $('#testcases-table').innerHTML=state.testCases.map(test=>`<tr><td><span class="mono">${test.testCaseId}</span></td><td><code>${escapeHtml(test.input)}</code></td><td><code>${escapeHtml(test.expectedOutput)}</code></td><td><strong>${test.marks}</strong></td></tr>`).join('');
    $('#testcases-empty').classList.toggle('hidden',state.testCases.length>0);
  }catch(error){notify(error.message,true);}
}

function renderRegistrations(){
  const filter=$('#registration-filter').value;
  const rows=state.registrations.filter(registration=>filter==='all'||!filter||String(registration.contestId)===filter);
  $('#registrations-empty').classList.toggle('hidden',rows.length>0);
  $('#registrations-table').innerHTML=rows.map(registration=>`<tr><td><span class="mono">${registration.registrationId}</span></td><td><strong>${escapeHtml(studentName(registration.studentId))}</strong><small class="cell-sub">#${registration.studentId}</small></td><td>${escapeHtml(contestName(registration.contestId))}</td><td>${escapeHtml(formatDate(registration.registrationDate))}</td><td><span class="status-pill ${registration.status==='Registered'?'upcoming':'completed'}">${escapeHtml(registration.status)}</span></td><td>${registration.status==='Registered'?`<button class="table-action" data-cancel="${registration.contestId}:${registration.studentId}">Cancel</button>`:''}</td></tr>`).join('');
}

function renderSubmissions(){
  $('#submission-count').textContent=state.submissions.length;
  $('#submissions-empty').classList.toggle('hidden',state.submissions.length>0);
  $('#submissions-table').innerHTML=state.submissions.slice().reverse().map(item=>`<tr><td><span class="mono">#${item.submissionId}</span></td><td>${escapeHtml(studentName(item.studentId))}</td><td>${escapeHtml(contestName(item.contestId))}</td><td>${escapeHtml(problemName(item.problemId))}</td><td>${escapeHtml(formatDate(item.submissionTime))}</td><td><span class="status-pill ${item.status==='Accepted'?'upcoming':''}">${escapeHtml(item.status)}</span></td><td>${item.score==null?'—':item.score}</td></tr>`).join('');
}

async function renderEvaluationCases(){
  const submissionId=$('#evaluation-submission').value;
  const submission=state.submissions.find(item=>String(item.submissionId)===submissionId);
  if(!submission){$('#evaluation-cases').innerHTML='<p class="muted">Choose a submission to see its test cases.</p>';return;}
  try{
    const tests=await api(`/api/testcases?problemId=${submission.problemId}`);
    $('#evaluation-cases').innerHTML=tests.length?tests.map((test,index)=>`<label class="case-output"><span class="case-title">TEST CASE ${test.testCaseId} · ${test.marks} MARKS</span><textarea name="output${index}" rows="2" placeholder="Enter actual output"></textarea><small>Expected: <code>${escapeHtml(test.expectedOutput)}</code></small></label>`).join(''):'<p class="muted">This problem has no test cases yet.</p>';
  }catch(error){notify(error.message,true);}
}

function studentName(id){const item=state.students.find(student=>student.studentId===id);return item?item.name:`Student #${id}`;}
function contestName(id){const item=state.contests.find(contest=>contest.contestId===id);return item?item.name:`Contest #${id}`;}
function problemName(id){const item=state.problems.find(problem=>problem.problemId===id);return item?item.title:`Problem #${id}`;}
function formatDate(value){return String(value||'').replace('T',' ').slice(0,16);}

function showPage(name){
  $$('.page').forEach(page=>page.classList.toggle('active',page.id===`page-${name}`));
  $$('.nav-item').forEach(item=>item.classList.toggle('active',item.dataset.page===name));
  const active=$(`.nav-item[data-page="${name}"]`);
  $('#page-crumb').textContent=active?active.textContent.trim():'Overview';
  if(name==='problems')renderProblems();
  if(name==='registrations')renderRegistrations();
}

function formObject(form){return Object.fromEntries(new FormData(form).entries());}

async function handleForm(form,endpoint,onSuccess){
  form.addEventListener('submit',async event=>{
    event.preventDefault();
    const button=form.querySelector('button[type="submit"]');
    button.disabled=true;
    try{
      await api(endpoint,formObject(form));
      form.reset();
      await loadAll();
      if(onSuccess)await onSuccess();
      notify('Saved successfully.');
    }catch(error){notify(error.message,true);}
    finally{button.disabled=false;}
  });
}

$$('.nav-item').forEach(button=>button.addEventListener('click',()=>showPage(button.dataset.page)));
$$('[data-go]').forEach(button=>button.addEventListener('click',()=>showPage(button.dataset.go)));

handleForm($('#student-form'),'/api/students');
handleForm($('#contest-form'),'/api/contests');
handleForm($('#problem-form'),'/api/problems');
handleForm($('#testcase-form'),'/api/testcases',renderTestCases);
handleForm($('#registration-form'),'/api/registrations');
handleForm($('#submission-form'),'/api/submissions');

$('#problem-filter').addEventListener('change',()=>{
  renderProblems();
  updateProblemOptions($('#testcase-problem'),state.problems,'Choose a problem');
});
$('#testcase-filter').addEventListener('change',renderTestCases);
$('#registration-filter').addEventListener('change',renderRegistrations);
$('#submission-contest').addEventListener('change',()=>updateProblemOptions($('#submission-problem'),state.problems,'Choose a problem'));
$('#plagiarism-contest').addEventListener('change',()=>updateProblemOptions($('#plagiarism-problem'),state.problems,'Choose a problem'));
$('#evaluation-submission').addEventListener('change',()=>{
  $('#evaluation-result').classList.add('hidden');
  renderEvaluationCases();
});

$('#registrations-table').addEventListener('click',async event=>{
  const button=event.target.closest('[data-cancel]');
  if(!button)return;
  const [contestId,studentId]=button.dataset.cancel.split(':');
  try{await api('/api/registrations/cancel',{contestId,studentId});await loadAll();notify('Registration cancelled.');}
  catch(error){notify(error.message,true);}
});

$('#students-table').addEventListener('click',async event=>{
  const edit=event.target.closest('[data-edit-student]');
  const remove=event.target.closest('[data-delete-student]');
  try{
    if(edit){
      const student=state.students.find(item=>String(item.studentId)===edit.dataset.editStudent);
      const name=prompt('Student name',student.name);
      if(name===null)return;
      const email=prompt('Student email',student.email);
      if(email===null)return;
      await api('/api/students/update',{studentId:student.studentId,name,email});
      await loadAll();notify('Student updated.');
    }else if(remove&&confirm('Remove this student account?')){
      await api('/api/students/delete',{studentId:remove.dataset.deleteStudent});
      await loadAll();notify('Student removed.');
    }
  }catch(error){notify(error.message,true);}
});

$('#contest-cards').addEventListener('click',async event=>{
  const edit=event.target.closest('[data-edit-contest]');
  const remove=event.target.closest('[data-delete-contest]');
  try{
    if(edit){
      const contest=state.contests.find(item=>String(item.contestId)===edit.dataset.editContest);
      const name=prompt('Contest name',contest.name);if(name===null)return;
      const description=prompt('Contest description',contest.description);if(description===null)return;
      const startTime=prompt('Start time (yyyy-MM-ddTHH:mm)',contest.startTime.slice(0,16));if(startTime===null)return;
      const endTime=prompt('End time (yyyy-MM-ddTHH:mm)',contest.endTime.slice(0,16));if(endTime===null)return;
      await api('/api/contests/update',{contestId:contest.contestId,name,description,startTime,endTime});
      await loadAll();notify('Contest updated.');
    }else if(remove&&confirm('Remove this contest? Its linked records will remain in memory.')){
      await api('/api/contests/delete',{contestId:remove.dataset.deleteContest});
      await loadAll();notify('Contest removed.');
    }
  }catch(error){notify(error.message,true);}
});

$('#problem-cards').addEventListener('click',async event=>{
  const edit=event.target.closest('[data-edit-problem]');
  const remove=event.target.closest('[data-delete-problem]');
  try{
    if(edit){
      const problem=state.problems.find(item=>String(item.problemId)===edit.dataset.editProblem);
      const title=prompt('Problem title',problem.title);if(title===null)return;
      const description=prompt('Problem description',problem.description);if(description===null)return;
      const difficulty=prompt('Difficulty: Easy, Medium, or Hard',problem.difficulty);if(difficulty===null)return;
      const maxScore=prompt('Maximum score',problem.maxScore);if(maxScore===null)return;
      const sampleInput=prompt('Sample input',problem.sampleInput||'');if(sampleInput===null)return;
      const sampleOutput=prompt('Sample output',problem.sampleOutput||'');if(sampleOutput===null)return;
      await api('/api/problems/update',{problemId:problem.problemId,title,description,difficulty,maxScore,sampleInput,sampleOutput});
      await loadAll();notify('Problem updated.');
    }else if(remove&&confirm('Remove this problem? Its submissions will remain in memory.')){
      await api('/api/problems/delete',{problemId:remove.dataset.deleteProblem});
      await loadAll();notify('Problem removed.');
    }
  }catch(error){notify(error.message,true);}
});

$('#evaluation-form').addEventListener('submit',async event=>{
  event.preventDefault();
  const form=event.currentTarget;
  const data=formObject(form);
  const button=form.querySelector('button[type="submit"]');
  button.disabled=true;
  try{
    const result=await api('/api/evaluate',data);
    const card=$('#evaluation-result');
    card.classList.remove('hidden','failed');
    if(result.status==='Failed')card.classList.add('failed');
    card.innerHTML=`<strong>${escapeHtml(result.status)}</strong><br>${result.passedTestCases} of ${result.totalTestCases} cases matched · ${result.score} marks<br><small>Output comparison time: ${result.comparisonTimeNanos} ns</small>`;
    await loadAll();
    $('#evaluation-submission').value=data.submissionId;
    await renderEvaluationCases();
    notify('Evaluation result saved.');
  }catch(error){notify(error.message,true);}
  finally{button.disabled=false;}
});

$('#load-leaderboard').addEventListener('click',async()=>{
  const contestId=$('#leaderboard-contest').value;
  if(!contestId){notify('Create a contest first.',true);return;}
  try{
    const entries=await api(`/api/leaderboard?contestId=${encodeURIComponent(contestId)}`);
    $('#leaderboard-table').innerHTML=entries.map((entry,index)=>`<tr><td><span class="rank-badge ${index<3?'top':''}">${String(index+1).padStart(2,'0')}</span></td><td><strong>${escapeHtml(entry.studentName)}</strong></td><td><b>${entry.score}</b></td><td>${escapeHtml(formatDate(entry.submissionTime))}</td></tr>`).join('');
    $('#leaderboard-empty').classList.toggle('hidden',entries.length>0);
    if(!entries.length)$('#leaderboard-empty').textContent='No evaluated submissions for this contest yet.';
  }catch(error){notify(error.message,true);}
});

$('#plagiarism-form').addEventListener('submit',async event=>{
  event.preventDefault();
  const form=event.currentTarget;
  const values=formObject(form);
  try{
    const report=await api(`/api/plagiarism?${new URLSearchParams(values)}`);
    $('#plagiarism-placeholder').classList.add('hidden');
    const target=$('#plagiarism-report');
    target.classList.remove('hidden');
    const matches=report.matches.map(match=>`<div class="match-card ${match.reviewRecommended?'suspicious':''}"><div><strong>Submission #${match.submissionIdA} <span class="muted">vs</span> #${match.submissionIdB}</strong><small>${match.reviewRecommended?'Review recommended':'Below review threshold'}</small></div><span class="similarity ${match.reviewRecommended?'flag':''}">${Number(match.similarity).toFixed(1)}%</span></div>`).join('');
    target.innerHTML=`<p class="eyebrow">SIMILARITY REPORT</p><h2>${escapeHtml(contestName(Number(values.contestId)))} · ${escapeHtml(problemName(Number(values.problemId)))}</h2><div class="report-summary"><div class="report-stat"><strong>${report.totalComparisons}</strong><span>COMPARISONS</span></div><div class="report-stat"><strong>${report.suspiciousMatches}</strong><span>REVIEW FLAGS</span></div></div>${matches||'<p class="muted">At least two submissions are needed for a comparison.</p>'}`;
    notify('Similarity report ready.');
  }catch(error){notify(error.message,true);}
});

loadAll().catch(error=>notify(error.message,true));