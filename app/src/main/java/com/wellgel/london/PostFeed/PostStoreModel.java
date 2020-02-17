package com.wellgel.london.PostFeed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostStoreModel {


    public class Data {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("post")
        @Expose
        private Post post;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Post getPost() {
            return post;
        }

        public void setPost(Post post) {
            this.post = post;
        }

    }

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("code")
        @Expose
        private Integer code;
        @SerializedName("data")
        @Expose
        private Data data;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }



    public class Post {

        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("media")
        @Expose
        private String media;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("post_like")
        @Expose
        private Integer postLike;
        @SerializedName("post_dislike")
        @Expose
        private Integer postDislike;
        @SerializedName("post_comment")
        @Expose
        private Integer postComment;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMedia() {
            return media;
        }

        public void setMedia(String media) {
            this.media = media;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getPostLike() {
            return postLike;
        }

        public void setPostLike(Integer postLike) {
            this.postLike = postLike;
        }

        public Integer getPostDislike() {
            return postDislike;
        }

        public void setPostDislike(Integer postDislike) {
            this.postDislike = postDislike;
        }

        public Integer getPostComment() {
            return postComment;
        }

        public void setPostComment(Integer postComment) {
            this.postComment = postComment;
        }

    }


}
