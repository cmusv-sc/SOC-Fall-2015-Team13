$(function(){

    $("#postList .panel-default .panel-body div :button").click(function(){
        var postId = $(this).attr("data-value");
        var divId = "post_"+postId;
        //$("#" + divId).hide();
        $.post("/network/post/delete", {"postId": postId}, function(data) {
        });
        $("#"+divId).hide();
    });

});

