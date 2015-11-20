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

    $("#postList .panel-default .row .panel-body div button").click(function(data) {
        var postId = $(this).attr("data-value");
        var content = $("#userComment_" + postId).val();
        $.post("/network/comment", {"postId": postId, "content": content}, function(data) {
            $("#comment_" + postId).append("<div class=\"row\"><div class=\"col-sm-2 col-md-2 col-lg-2\">" +
                "<div class='thumbnail'>" +
                "<img class=\"img-responsive user-photo\" src=\"http://www.wpclipart.com/signs_symbol/speech_bubbles/speech_gradients/comment_bubble_gradient_blue_right_T.png\"></div></div>" +
                "<div class=\"col-sm-10 col-lg-10 col-md-10\">" +
                "<div class=\"panel panel-default\">" +
                "<div class=\"panel-heading\">" +
                "<strong>You</strong> <span class=\"text-muted\">" +
                "commented just now.</span></div>" +
                "<div class=\"panel-body\">" + content + "</div></div></div></div><hr>"
            )
        });
    });
});


