$(function(){

    $("#postList .panel-default .panel-body .row div .delete_button").click(function(){
        var postId = $(this).attr("data-value");
        var divId = "post_"+postId;
        $.post("/network/post/delete", {"postId": postId}, function(data) {
            $("#"+divId).hide();
        });
    });

    $("#postList .panel-default .panel-body .row div .fa-thumbs-o-up").click(function() {
        var likes = $(this).attr("data-value");
        var postId = $(this).attr("title");
        var newLikes = parseInt(likes) + 1;
        var idForLikes = "post_likes_" + postId;
        $.post("/network/post/update", {"postId": postId, "numOfLikes": newLikes}, function(data) {
            $("#"+idForLikes).html("Likes: " + newLikes);
            $(this).attr("data-value", newLikes);
        });
    });

    $("#postList .panel-default .panel-body .row .glyphicon-pencil").click(function() {
        var postId = $(this).attr("data-value");
        $("#post_content_"+postId).hide();
        $("#post_delete_"+postId).hide();
        $("#post_edit_"+postId).show();
        $("#post_update_"+postId).show();
    });

    $("#postList .panel-default .panel-body .row div .update_button").click(function(){
        var postId = $(this).attr("data-value");
        var content = $("#post_edit_content_" + postId).val();
        $.post("/network/post/update", {"postId": postId, "content": content}, function(data) {
            $("#post_edit_"+postId).hide();
            $("#post_update_"+postId).hide();
            $("#post_original_content_"+postId ).html(content);
            $("#post_content_"+postId).show();
            $("#post_delete_"+postId).show();
        })
    });
});

