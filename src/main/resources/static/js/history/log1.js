$(function() {
	$("#job-exec-details-table").on("all.bs.table", function() {
        doLocale();
    });
     bindShardingStatusButton();

});

function bindShardingStatusButton() {
    $(document).off("click", "button[operation='job-detail'][data-toggle!='modal']");
    $(document).on("click", "button[operation='job-detail'][data-toggle!='modal']", function(event) {
        var jobName = $(event.currentTarget).attr("job-name");
        $("#content").load("html/history/log.html", null, function(){
            $("#job-name").val(jobName);
            $("#job-exec-details-table").attr("data-url","api/log?jobName="+jobName);
            $("#job-exec-details-table").bootstrapTable("refresh", {silent: true});
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

function successFormatter(value) {
	if(value==1){
		value=true;
	}else{
		value=false;
	}
    switch(value)
    {
    case true:
        return "<span class='label label-success' data-lang='execute-result-success'></span>";
      case false:
          return "<span class='label label-danger' data-lang='execute-result-failure'></span>";
      default:
        return "<span class='label label-danger' data-lang='execute-result-null'></span>";
    }
}

function splitFormatter(value) {
    var maxLength = 50;
    var replacement = "...";
    if(null != value && value.length > maxLength) {
        var vauleDetail = value.substring(0 , maxLength - replacement.length) + replacement;
        value = value.replace(/\r\n/g,"<br/>").replace(/\n/g,"<br/>").replace(/\'/g, "\\'");
        return '<a href="javascript: void(0);" style="color:#FF0000;" onClick="showHistoryMessage(\'' + value + '\')">' + vauleDetail + '</a>';
    }
    return value;
}
