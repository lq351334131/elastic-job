$(function() {
	 $("#job-exec-status-table").on("all.bs.table", function() {
	        doLocale();
	    });
     bindShardingStatusButton();
});


function bindShardingStatusButton() {
    $(document).off("click", "button[operation='job-detail'][data-toggle!='modal']");
    $(document).on("click", "button[operation='job-detail'][data-toggle!='modal']", function(event) {
        var jobName = $(event.currentTarget).attr("job-name");
        $("#content").load("html/history/job_status_history.html", null, function(){
            $("#job-name").val(jobName);
            $("#job-exec-status-table").bootstrapTable("refresh", {silent: true});
            $("#job-exec-status-table").attr("data-url","api/event-trace/status?jobName="+jobName);
            doLocale();
        });
    });
}

function  generateOperationButtons(val,row){
    var shardingStatusButton = "<button operation='job-detail' class='btn-xs btn-info' job-name='" + row.jobName + "' data-lang='operation-detail'></button>";
 return  shardingStatusButton;
	
}
function queryParams(params) {
    var sortName = "success" === params.sortName ? "isSuccess" : params.sortName;
    return {
        per_page: params.pageSize,
        page: params.pageNumber,
        q: params.searchText,
        sort: sortName,
        order: params.sortOrder,
        jobName: $("#job-name").val()
    };
}

function splitRemarkFormatter(value, row) {
    var maxLength = 50;
    var replacement = "...";
    if(null != value && value.length > maxLength) {
        var valueDetail = value.substring(0 , maxLength - replacement.length) + replacement;
        value = value.replace(/\r\n/g,"<br/>").replace(/\n/g,"<br/>").replace(/\'/g, "\\'");
        var remarkHtml;
        if ("TASK_FAILED" === row.state || "TASK_ERROR" === row.state) {
            remarkHtml = '<a href="javascript: void(0);" style="color:#FF0000;" onClick="showHistoryMessage(\'' + value + '\')">' + valueDetail + '</a>';
        } else {
            remarkHtml = '<a href="javascript: void(0);" style="color:black;" onClick="showHistoryMessage(\'' + value + '\')">' + valueDetail + '</a>';
        }
        return remarkHtml;
    }
    return value;
}

function stateFormatter(value) {
    switch(value)
    {
        case "TASK_STAGING":
            return "<span class='label label-default' data-lang='status-staging'></span>";
        case "TASK_FAILED":
            return "<span class='label label-danger' data-lang='status-task-failed'></span>";
        case "TASK_FINISHED":
            return "<span class='label label-success' data-lang='status-task-finished'></span>";
        case "TASK_RUNNING":
            return "<span class='label label-primary' data-lang='status-running'></span>";
        case "TASK_ERROR":
            return "<span class='label label-danger' data-lang='status-task-error'></span>";
        case "TASK_KILLED":
            return "<span class='label label-warning' data-lang='status-task-killed'></span>";
        default:
            return "-";
    }
}
