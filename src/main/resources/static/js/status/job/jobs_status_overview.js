$(function() {
    authorityControl();
    renderJobsOverview();
    bindButtons();
});

function renderJobsOverview() {
    var jsonData = {
        cache: false,
        search: true,
        showRefresh: true,
        showColumns: true
    };
    var activated = false;
    jsonData.url = "/api/jobs";
    console.log("jsonData : " + jsonData);
    $("#jobs-status-overview-tbl").bootstrapTable({
        columns: jsonData.columns,
        url: jsonData.url,
        cache: jsonData.cache
    }).on("all.bs.table", function() {
        doLocale();
    });
}

function statusFormatter(value, row) {
    switch(value) {
        case "OK":
            return "<span class='label label-success' data-lang='status-ok'></span>";
            break;
        case "DISABLED":
            return "<span class='label label-warning' data-lang='status-disabled'></span>";
            break;
        case "SHARDING_FLAG":
            return "<span class='label label-info' data-lang='status-sharding-flag'></span>";
            break;
        case "CRASHED":
            return "<span class='label label-default' data-lang='status-crashed'></span>";
            break;
    }
}

function generateOperationButtons(val, row) {
    var modifyButton = "<button operation='modify-job' class='btn-xs btn-primary' job-name='" + row.jobName + "' data-lang='operation-update'></button>";
    var shardingStatusButton = "<button operation='job-detail' class='btn-xs btn-info' job-name='" + row.jobName + "' data-lang='operation-detail'></button>";
    var triggerButton = "<button operation='trigger-job' class='btn-xs btn-success' job-name='" + row.jobName + "' data-lang='operation-trigger'></button>";
    var disableButton = "<button operation='disable-job' class='btn-xs btn-warning' job-name='" + row.jobName + "' data-lang='operation-disable'></button>";
    var enableButton = "<button operation='enable-job' class='btn-xs btn-success' job-name='" + row.jobName + "' data-lang='operation-enable'></button>";
    var shutdownButton = "<button operation='shutdown-job' class='btn-xs btn-danger' job-name='" + row.jobName + "' data-lang='operation-shutdown'></button>";
    var removeButton = "<button operation='remove-job' class='btn-xs btn-danger' job-name='" + row.jobName + "' data-lang='operation-remove'></button>";
    var operationTd = modifyButton + "&nbsp;" + shardingStatusButton  + "&nbsp;";
    if ("OK" === row.status) {
        operationTd = operationTd + triggerButton + "&nbsp;" + disableButton + "&nbsp;" + shutdownButton;
    }
    if ("DISABLED" === row.status) {
        operationTd = operationTd + enableButton + "&nbsp;" + shutdownButton;
    }
    if ("SHARDING_FLAG" === row.status) {
        operationTd = operationTd + "&nbsp;" + shutdownButton;
    }
    if ("CRASHED" === row.status) {
        operationTd = modifyButton + "&nbsp;" + removeButton;
    }
    return operationTd;
}

function bindButtons() {
    bindModifyButton();
    bindShardingStatusButton();
    bindTriggerButton();
    bindShutdownButton();
    bindDisableButton();
    bindEnableButton();
    bindRemoveButton();
    dealRegCenterModal();
    submitRegCenter();
    validate();
}

function bindModifyButton() {
    $(document).off("click", "button[operation='modify-job'][data-toggle!='modal']");
    $(document).on("click", "button[operation='modify-job'][data-toggle!='modal']", function(event) {
        var jobName = $(event.currentTarget).attr("job-name");
        $.ajax({
            url: "/api/jobs/config/" + jobName,
            success: function(data) {
                if (null !== data) {
                    $(".box-body").remove();
                    $('#update-job-body').load('html/status/job/job_config.html', null, function() {
                        doLocale();
                        $('#data-update-job').modal({backdrop : 'static', keyboard : true});
                        renderJob(data);
                        $("#job-overviews-name").text(jobName);
                    });
                }
            }
        });
    });
}


function dealRegCenterModal() {
    $("#add-register").click(function() {
        $("#add-reg-center").modal({backdrop: 'static', keyboard: true});
        $("#addInput1").nextAll().remove();
    });
    $("#close-add-reg-form").click(function() {
        $("#add-reg-center").on("hide.bs.modal", function () {
            $("#reg-center-form")[0].reset();
        });
        $("#reg-center-form").data("bootstrapValidator").resetForm();
    });
}


function submitRegCenter() {

    $("#add-reg-center-btn").on("click", function(event) {
        if ("" === $("#digest").val()) {
            $("#reg-center-form").data("bootstrapValidator").enableFieldValidators("digest", false);
        }
        if ("" === $("#namespace").val()) {
            $("#reg-center-form").data("bootstrapValidator").enableFieldValidators("namespace", false);
        }
        var bootstrapValidator = $("#reg-center-form").data("bootstrapValidator");
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()) {
            var jobName = $("#jobName").val();
    		var jobType=$("#jobType option:selected").val();
            var parameter ="";// $("#parameter").val();
            var cron = $("#cron").val();
            var shardingTotalCount = $("#shardingTotalCount").val();
            var url = $("#url").val();
            var type = $("#reg-center-form #type  option:selected").val();
            var $test = $("input[name=name1]");//假设name为test
            var $value = $("input[name=value]");//假设name为test
            
            var object={};
        	for(var i=0;i<$test.length;i++){
        		object[$test.eq(i).val()]=$value.eq(i).val();
        	}
        	 parameter=JSON.stringify(object); ;
        	 console.log(parameter);
            
            
        	 var param = JSON.stringify({"jobName": jobName, "jobType": jobType, "parmeter": {"type":type,"parameter":parameter,"url":url}, "cron": cron,"shardingTotalCount":shardingTotalCount})
        	 console.log("param : " + param)
            $.ajax({
                url: "api/add-job",
                type: "POST",
                data: param,
                //data: JSON.stringify({"jobName": jobName, "jobType": jobType, "parameter": parameter, "cron": cron,"shardingTotalCount":shardingTotalCount,"type":type,"url":url}),
                contentType: "application/json",
                dataType: "json",
                success: function(data) {
                    console.log(data);
                    if (data.code==200) {
                        $("#add-reg-center").on("hide.bs.modal", function() {
                            $("#reg-center-form")[0].reset();
                        });
                        $("#reg-center-form").data("bootstrapValidator").resetForm();
                        $("#add-reg-center").modal("hide");
                        $("#reg-centers").bootstrapTable("refresh");
                        $(".modal-backdrop").remove();
                        $("body").removeClass("modal-open");
                        refreshJobNavTag();
                        refreshServerNavTag();
                        $("#jobs-status-overview-tbl").bootstrapTable("refresh");
                    }else{
                        //alert(data.message);
                        showFailureDialog(data.message);
                        
                        $(this).button("loading").button("reset");
                    }
                }
            });
        }
    });
}



function bindShardingStatusButton() {
    $(document).off("click", "button[operation='job-detail'][data-toggle!='modal']");
    $(document).on("click", "button[operation='job-detail'][data-toggle!='modal']", function(event) {
        var jobName = $(event.currentTarget).attr("job-name");
        $("#index-job-name").text(jobName);
        $("#content").load("html/status/job/job_status_detail.html", null, function(){
            doLocale();
        });
    });
}

function bindTriggerButton() {
    $(document).off("click", "button[operation='trigger-job'][data-toggle!='modal']");
    $(document).on("click", "button[operation='trigger-job'][data-toggle!='modal']", function(event) {
        var jobName = $(event.currentTarget).attr("job-name");
        $.ajax({
            url: "/api/jobs/" + jobName + "/trigger",
            type: "POST",
            success: function(data) {
            	if(data.code=="200"){
            		 showSuccessDialog();
            	}else{
            		console.log(data.code+"=="+data.message);
            		showInfoDialog($.i18n.prop("operation-error"));
            	}
            	
            	$("#jobs-status-overview-tbl").bootstrapTable("refresh");	
               
            }
        });
    });
}

function bindDisableButton() {
    $(document).off("click", "button[operation='disable-job'][data-toggle!='modal']");
    $(document).on("click", "button[operation='disable-job'][data-toggle!='modal']", function(event) {
        var jobName = $(event.currentTarget).attr("job-name");
        $.ajax({
            url: "/api/jobs/" + jobName + "/disable",
            type: "POST",
            success: function(data) {
            	if(data.code=="200"){
           		  showSuccessDialog();
             	}else{
            	  console.log(data.code+"=="+data.message);
           		  showInfoDialog($.i18n.prop("operation-error"));
           	   }
                $("#jobs-status-overview-tbl").bootstrapTable("refresh");
            }
        });
    });
}

function bindEnableButton() {
    $(document).off("click", "button[operation='enable-job'][data-toggle!='modal']");
    $(document).on("click", "button[operation='enable-job'][data-toggle!='modal']", function(event) {
        var jobName = $(event.currentTarget).attr("job-name");
        $.ajax({
            url: "/api/jobs/" + jobName + "/disable",
            type: "DELETE",
            success: function(data) {
            	if(data.code=="200"){
           		  showSuccessDialog();
           	    }else{
            	  console.log(data.code+"=="+data.message);
           		  showInfoDialog($.i18n.prop("operation-error"));
           	    }
                $("#jobs-status-overview-tbl").bootstrapTable("refresh");
            }
        });
    });
}

function bindShutdownButton() {
    $(document).off("click", "button[operation='shutdown-job'][data-toggle!='modal']");
    $(document).on("click", "button[operation='shutdown-job'][data-toggle!='modal']", function(event) {
        showShutdownConfirmModal();
        var jobName = $(event.currentTarget).attr("job-name");
        $(document).off("click", "#confirm-btn");
        $(document).on("click", "#confirm-btn", function() {
            $.ajax({
                url: "/api/jobs/" + jobName + "/shutdown",
                type: "POST",
                success: function () {
                    $("#confirm-dialog").modal("hide");
                    $(".modal-backdrop").remove();
                    $("body").removeClass("modal-open");
                    $("#jobs-status-overview-tbl").bootstrapTable("refresh");
                    refreshJobNavTag();
                    refreshServerNavTag();
                }
            });
        });
    });
}

function bindRemoveButton() {
    $(document).off("click", "button[operation='remove-job'][data-toggle!='modal']");
    $(document).on("click", "button[operation='remove-job'][data-toggle!='modal']", function(event) {
        var jobName = $(event.currentTarget).attr("job-name");
        showDeleteConfirmModal();
        $(document).off("click", "#confirm-btn");
        $(document).on("click", "#confirm-btn", function() {
            $.ajax({
                url: "/api/jobs/config/" + jobName,
                type: "DELETE",
                success: function() {
                    $("#confirm-dialog").modal("hide");
                    $(".modal-backdrop").remove();
                    $("body").removeClass("modal-open");
                    refreshJobNavTag();
                    refreshServerNavTag();
                    $("#jobs-status-overview-tbl").bootstrapTable("refresh");
                }
            });
        });
    });
}

function renderJob(data) {
    $("#job-name").attr("value", data.jobName);
    $("#job-type").attr("value", data.jobType);
    $("#job-class").attr("value", data.jobClass);
    $("#sharding-total-count").attr("value", data.shardingTotalCount);
    $("#cron").attr("value", data.cron);
    $("#sharding-item-parameters").text(data.shardingItemParameters);
	    if(data.jobName!="updateLogJob"){
	    
	    var jsonObj = eval('(' + data.jobParameter + ')');
	    //$("#parameter").attr("value", jsonObj.parameter);
	    
	    $("#url").attr("value", jsonObj.url);
	    $("#type").attr("value", jsonObj.type);
	    var  json=[];
	    var p= JSON.parse(jsonObj.parameter);
	    console.log(p);
	    console.log(eval('(' + jsonObj.parameter + ')'));
	    for( key in  p){
	    	json.push({"name1":key,"value": p[key]});
	    }
	    
	    console.log(json);
	    for(var i=0;i<json.length;i++){
	    	console.log(json[i].name1+"=="+json[i].value);
	    	if(i==0){
	    		$("#addInput_1").find("#n1").val(json[i].name1);
	    		$("#addInput_1").find("#v1").val(json[i].value);
	
	    	}else{
	    		 var   nadd=i+1;
	    	   	 var   did="addInput_"+nadd;
	    		 var input=`
	    		   	 <div  class="form-group text-center" id=`+did+` >
	    		   	  参数名称 :<input type="text" class=""  name="name2" value="`+ json[i]["name1"]+`">
	    		     参数值 :    <input type="text" class=""  name="value2" value="`+json[i]["value"] +`">
	    		          <button  type="button" class="btn-xs btn-danger " onclick="removeButton1(`+nadd+`)" >删除</button>
	    		       </div>` ;
	    		$('div[id^=addInput_]:last').after(input);
	    		
	    	}
	    	
	     }
	    
	    
	    
	   }else{
		   $('div[id^=addInput_]:last').remove();
		   $("#job-config-form #div_url").remove();
		   $("#job-config-form #div_type").remove();
		   
		   
	   }
	    
   
    
    $("#monitor-execution").attr("checked", data.monitorExecution);
    $("#failover").attr("checked", data.failover);
    $("#misfire").attr("checked", data.misfire);
    $("#streaming-process").attr("checked", data.streamingProcess);
    $("#max-time-diff-seconds").attr("value", data.maxTimeDiffSeconds);
    $("#monitor-port").attr("value", data.monitorPort);
    $("#job-sharding-strategy-class").attr("value", data.jobShardingStrategyClass);
    $("#executor-service-handler").attr("value", data.jobProperties["executor_service_handler"]);
    $("#job-exception-handler").attr("value", data.jobProperties["job_exception_handler"]);
    $("#reconcile-interval-minutes").attr("value", data.reconcileIntervalMinutes);
    $("#description").text(data.description);
    $("#script-command-line").attr("value", data.scriptCommandLine);
    if ("DATAFLOW" === $("#job-type").val()) {
        $("#streaming-process-group").show();
    }
    if ("SCRIPT" === $("#job-type").val()) {
        $("#script-commandLine-group").show();
    }
}

function validate() {
    $("#reg-center-form").bootstrapValidator({
        message: "This value is not valid",
        feedbackIcons: {
            valid: "glyphicon glyphicon-ok",
            invalid: "glyphicon glyphicon-remove",
            validating: "glyphicon glyphicon-refresh"
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {
                        message: $.i18n.prop("registry-center-name-not-null")
                    },
                    stringLength: {
                        max: 50,
                        message: $.i18n.prop("registry-center-name-length-limit")
                    },
                    callback: {
                        message: $.i18n.prop("registry-center-existed"),
                        callback: function() {
                            var regName = $("#name").val();
                            var result = true;
                            $.ajax({
                                url: "api/registry-center",
                                contentType: "application/json",
                                async: false,
                                success: function(data) {
                                    for (var index = 0; index < data.length; index++) {
                                        if (regName === data[index].name) {
                                            result = false;
                                        }
                                    }
                                }
                            });
                            return result;
                        }
                    }
                }
            },
            zkAddressList: {
                validators: {
                    notEmpty: {
                        message: $.i18n.prop("registry-center-zk-address-not-null")
                    },
                    stringLength: {
                        max: 100,
                        message: $.i18n.prop("registry-center-zk-address-length-limit")
                    }
                }
            },
            namespace: {
                validators: {
                    stringLength: {
                        max: 50,
                        message: $.i18n.prop("registry-center-namespace-length-limit")
                    }
                }
            },
            digest: {
                validators: {
                    stringLength: {
                        max: 20,
                        message: $.i18n.prop("registry-center-digest-length-limit")
                    }
                }
            }
        }
    });
    $("#reg-center-form").submit(function(event) {
        event.preventDefault();
    });
    
    
    
}


function  removeButton(did){
	$("#addInput"+did).remove();
	
}

$("#addButton").click(function() {
	var add= $('div[id^=addInput]:last').attr("id");
	var add=add.replace("addInput","");
	 var   nadd=parseInt(add)+1;
	 var  nid="n"+nadd;
	 var  vid="v"+nadd;
	 var   did="addInput"+nadd;
	 var input=`
	 <div  class="form-group" id=`+did+` >
	    参数名称:<input type="text" class="" id=`+ nid +` name="name1" value="">
       参数值: <input type="text" class="" id=`+ vid +` name="value" value="">
       <button  type="button" class="btn-xs btn-danger " onclick="removeButton(`+nadd+`)" >删除</button>
    </div>` ;
	$('div[id^=addInput]:last').after(input);
	
	
    
});






