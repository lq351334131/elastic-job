$(function() {
    $("#content").load("../html/status/job/jobs_status_overview.html");
    $("#job-status").click(function() {
        $("#content").load("../html/status/job/jobs_status_overview.html");
    });
    $("#status-history").click(function() {
        $("#content").load("../html/history/job_status_history1.html");
    });
    $("#log").click(function() {
        $("#content").load("../html/history/log1.html");
    });
    $("#help").click(function() {
        $("#content").load("../html/help/help.html", null, function(){
            doLocale();
        });
    });
    switchLanguage();

    //初始化显示语言
    initLanguage();
    
  
});
