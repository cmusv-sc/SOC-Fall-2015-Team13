$(function(){

    $("#postList .panel-default .panel-body .row div :button").click(function(){
        var postId = $(this).attr("data-value");
        var divId = "post_"+postId;
        $.post("/network/post/delete", {"postId": postId}, function(data) {
        });
        $("#"+divId).hide();
    });

    $("#postList .panel-default .panel-body .row div div").click(function() {
        var likes = $(this).attr("data-value");
        var postId = $(this).attr("title");
        var newLikes = parseInt(likes) + 1;
        var idForLikes = "post_likes_" + postId;
        $.post("/network/post/update", {"postId": postId, "numOfLikes": newLikes}, function(data) {
        });
        $("#"+idForLikes).html("Likes: " + newLikes);
    })

});

