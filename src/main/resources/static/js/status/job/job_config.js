$(function() {
    tooltipLocale();
    validate();
    bindSubmitJobSettingsForm();
    bindResetForm();
   
});

function tooltipLocale(){
    for (var i = 0; i < $("[data-toggle='tooltip']").length; i++) {
        var object = $("[data-toggle='tooltip']")[i];
        $(object).attr('title',$.i18n.prop("placeholder-" + object.getAttribute("id"))).tooltip('fixTitle');
    }
}

function getJobParams() {
    var jobName = $("#job-overviews-name").text();
    var jobParams;
    $.ajax({
        url: "/api/jobs/config/" + jobName,
        async: false,
        success: function(data) {
            jobParams = data;
        }
    });
    return jobParams;
}

function bindSubmitJobSettingsForm() {
    $("#update-job-info-btn").on("click", function(){
        var bootstrapValidator = $("#job-config-form").data("bootstrapValidator");
        bootstrapValidator.validate();
        if (bootstrapValidator.isValid()) {
            var jobName = $("#job-name").val();
            var jobType = $("#job-type").val();
            var jobClass = $("#job-class").val();
            var shardingTotalCount = $("#sharding-total-count").val();
            var parameter ="";// $("#parameter").val();
            var url = $("#url").val();
            var type = $("#job-config-form #type").val();
            
            var $test = $("input[name=name2]");//假设name为test
            var $value = $("input[name=value2]");//假设name为test
            var object={};
        	for(var i=0;i<$test.length;i++){
        		object[$test.eq(i).val()]=$value.eq(i).val();
        	}
        	 parameter=JSON.stringify(object); ;
            var jobParameter1={"url":url,"type":type,"parameter":parameter};
            var jobParameter=JSON.stringify(jobParameter1);
            var cron = $("#cron").val();
            var streamingProcess = $("#streaming-process").prop("checked");
            var maxTimeDiffSeconds = $("#max-time-diff-seconds").val();
            var monitorPort = $("#monitor-port").val();
            var monitorExecution = $("#monitor-execution").prop("checked");
            var failover = $("#failover").prop("checked");
            var misfire = $("#misfire").prop("checked");
            var driver = $("#driver").val();
            var url = $("#url").val();
            var username = $("#username").val();
            var password = $("#password").val();
            var logLevel = $("#logLevel").val();
            var shardingItemParameters = $("#sharding-item-parameters").val();
            var jobShardingStrategyClass = $("#job-sharding-strategy-class").val();
            var scriptCommandLine = $("#script-command-line").val();
            var executorServiceHandler = $("#executor-service-handler").val();
            var jobExceptionHandler = $("#job-exception-handler").val();
            var description = $("#description").val();
            var reconcileIntervalMinutes = $("#reconcile-interval-minutes").val();
            var postJson = {jobName: jobName, jobType : jobType, jobClass : jobClass, shardingTotalCount: shardingTotalCount, jobParameter: jobParameter, cron: cron, streamingProcess: streamingProcess, maxTimeDiffSeconds: maxTimeDiffSeconds, monitorPort: monitorPort, monitorExecution: monitorExecution, failover: failover, misfire: misfire, shardingItemParameters: shardingItemParameters, jobShardingStrategyClass: jobShardingStrategyClass, jobProperties: {"executor_service_handler": executorServiceHandler, "job_exception_handler": jobExceptionHandler}, description: description, scriptCommandLine: scriptCommandLine, reconcileIntervalMinutes:reconcileIntervalMinutes};
            var jobParams = getJobParams();
            submitAjax(postJson);
           /* if (jobParams.monitorExecution !== monitorExecution || jobParams.failover !== failover || jobParams.misfire !== misfire) {
                showUpdateConfirmModal();
                $(document).off("click", "#confirm-btn");
                $(document).on("click", "#confirm-btn", function() {
                    $("#confirm-dialog").modal("hide");
                    submitAjax(postJson);
                });
            } else {
                submitAjax(postJson);
            }*/
        }
    });
}

function submitAjax(postJson) {
    $("#job-config-form").data("bootstrapValidator").resetForm();
    $.ajax({
        url: "/api/jobs/config",
        type: "PUT",
        data: JSON.stringify(postJson),
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            $("#data-update-job").modal("hide");
            $("#jobs-status-overview-tbl").bootstrapTable("refresh");
            if (data.code==200){
                showSuccessDialog();
            }else{
                showFailureDialog('操作失败，请重试！');
            }
            
        }
    });
}

function validate() {
    $("#job-config-form").bootstrapValidator({
        message: "This value is not valid",
        feedbackIcons: {
            valid: "glyphicon glyphicon-ok",
            invalid: "glyphicon glyphicon-remove",
            validating: "glyphicon glyphicon-refresh"
        },
        fields: {
            shardingTotalCount: {
                validators: {
                    notEmpty: {
                        message: $.i18n.prop("job-sharding-count-not-null")
                    },
                    regexp: {
                        regexp: /^(-?\d+)?$/,
                        message: $.i18n.prop("job-sharding-count-should-be-integer")
                    }
                }
            },
            cron: {
                validators: {
                    stringLength: {
                        max: 40,
                        message: $.i18n.prop("job-cron-length-limit")
                    },
                    notEmpty: {
                        message: $.i18n.prop("job-cron-not-null")
                    }
                }
            },
            monitorPort: {
                validators: {
                    regexp: {
                        regexp: /^(-?\d+)?$/,
                        message: $.i18n.prop("job-monitor-port-should-be-integer")
                    },
                    notEmpty: {
                        message: $.i18n.prop("job-monitor-port-not-null")
                    },
                    callback: {
                        message: $.i18n.prop("job-monitor-port-range-limit"),
                        callback: function(value, validator) {
                            var monitorPort = parseInt(validator.getFieldElements("monitorPort").val(), 10);
                            if (monitorPort <= 65535) {
                                validator.updateStatus("monitorPort", "VALID");
                                return true;
                            }
                            return false;
                        }
                    }
                }
            }
        }
    });
    $("#job-config-form").submit(function(event) {
        event.preventDefault();
    });
}

function bindResetForm() {
    $("#reset").click(function() {
        $("#job-config-form").data("bootstrapValidator").resetForm();
    });
}


$("#addButton1").click(function() {
	
	var add= $('div[id^=addInput_]:last').attr("id");
	var add=add.replace("addInput_","");
	 var   nadd=parseInt(add)+1;
	 var  nid="n"+nadd;
	 var  vid="v"+nadd;
	 var   did="addInput_"+nadd;
	 var input=`
	 <div  class="form-group text-center" id=`+did+` >
	   参数名称:<input type="text" class="" id=`+ nid +` name="name2" value="">参数值:
       <input type="text" class="" id=`+ vid +` name="value2" value="">
       <button  type="button" class="btn-xs btn-danger " onclick="removeButton1(`+nadd+`)" >删除</button>
    </div>` ;
	$('div[id^=addInput_]:last').after(input);
	
    
});

function  removeButton1(did){
	$("#addInput_"+did).remove();
	
}







