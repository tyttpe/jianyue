<template>
	<view class="container">
		<text class="article-title">{{ article.title }}</text>
		<view class="article-info">
			<view class="article-info-top">
				<view class="info-top-left">
					<image :src="article.avatar" class="avatar small"></image>
					<text class="nickname">{{ article.nickname }}</text>
				</view>
				<view class="info-top-right">
					<button v-if="userId != article.uId && !followed" class="green-btn follow-btn" @tap="follow">关注</button>
					<button v-if="userId != article.uId && followed" class="green-btn follow-btn cancel" @tap="cancelFollow">取消</button>
				</view>
			</view>
			<view class="info-time">
				<text>字数 {{ handleContent(article.content).length }}</text>
				<text>{{handleTime(article.createTime) }}</text>
			</view>
		</view>

		<view class="grace-text" style="margin-top: 10px;">
			<rich-text :nodes="article.content" bindtap="tap"></rich-text>
		</view>

		<view class="info-bottom-right">
			<button v-if=" !liked" class="green-btn like-btn" @tap="like">赞</button>
			<button v-if=" liked" class="green-btn like-btn cancel2" @tap="cancelLike">取消</button>
		</view>

		<text class="info-text">评论 ({{ comments.length }})</text>
		<view class="comment-item" v-for="(comment, index) in comments" :key="index">
			<view class="left">
				<image :src="comment.avatar" class="avatar small"></image>
			</view>
			<view class="right">
				<text class="right-name">{{ comment.nickname }}</text>
				<text>{{ handleContent(comment.content) }}</text>
				<view class="top">
					<text style="margin-right: 10px;">{{ comments.length - index }}楼</text>
					<text>{{ handleTime(comment.commentTime) }}</text>
				</view>
			</view>
		</view>
		<textarea class="uni-input comment-box" placeholder="写下你的评论" v-model="content" required="required" />
		<button class="green-btn" @tap="send">提交</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				article: {
					aId: 0,
					uId: 0,
					title: '',
					content: '',
					avatar: '',
					nickname: '',
					createTime: ''
				},
				comments: [],
				content: '',
				userId: uni.getStorageSync('login_key').userId,
				// userId:0,
				followed: false,
				liked: false
			};
		},
		onLoad: function(option) {
			//option为object类型，会序列化上个页面传递的参数
			this.article.aId = option.aId;
		},
		onShow: function() {
			this.getArticle();
		},
		onPullDownRefresh: function() {
			this.getArticle();
		},
		methods: {
			getArticle: function() {
				var _this = this;
				uni.request({
					url: this.apiServer + '/article/' + this.article.aId,
					method: 'GET',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						userId: this.userId,
						aId:this.article.aId
					},
					success: res => {
						console.log(res.data.data.article);
						_this.article.aId = res.data.data.article.id;
						_this.article.uId = res.data.data.article.uId;
						_this.article.title = res.data.data.article.title;
						_this.article.content = res.data.data.article.content;
						_this.article.nickname = res.data.data.article.nickname;
						_this.article.avatar = res.data.data.article.avatar;
						_this.article.createTime = res.data.data.article.createTime;
						_this.comments = res.data.data.comments;
						if (res.data.data.followed === '已关注') {
							_this.followed = true;
						}
						if(res.data.data.liked === '赞'){
							_this.liked = true;
						}
					},
					complete: function() {
						uni.stopPullDownRefresh();
					}
				});
			},
			handleTime: function(date) {
				var d = new Date(date);
				var year = d.getFullYear();
				var month = d.getMonth() + 1;
				var day = d.getDate() < 10 ? '0' + d.getDate() : '' + d.getDate();
				var hour = d.getHours() < 10 ? '0' + d.getHours() : '' + d.getHours();
				var minutes = d.getMinutes() < 10 ? '0' + d.getMinutes() : '' + d.getMinutes();
				var seconds = d.getSeconds() < 10 ? '0' + d.getSeconds() : '' + d.getSeconds();
				return year + '-' + month + '-' + day + ' ' + hour + ':' + minutes + ':' + seconds;
			},
			handleContent: function(msg) {
				let description = msg;
				description = description.replace(/(\n)/g, "");
				description = description.replace(/(\t)/g, "");
				description = description.replace(/(\r)/g, "");
				description = description.replace(/<\/?[^>]*>/g, "");
				description = description.replace(/\s*/g, "");
				return description;
			},
			send: function() {
				console.log('评论人编号：' + this.userId + ',文章编号：' + this.article.aId + '，评论内容：' + this.content);
				uni.request({
					url: this.apiServer + '/comment/add',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						aId: this.article.aId,
						uId: this.userId,
						// uId: uni.getStorageSync('login_key').userId,
						content: this.content
					},
					success: res => {
						if (res.data.code === 0) {
							uni.showToast({
								title: '评论成功'
							});
							this.getArticle();
							this.content = '';
						}
					}
				});
			},
			follow: function() {
				uni.request({
					url: this.apiServer + '/follow/add',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						fromUId: this.userId,
						// fromUId: uni.getStorageSync('login_key').userId,
						toUId: this.article.uId
					},
					success: res => {
						if (res.data.code === 0) {
							uni.showToast({
								title: '关注成功'
							});
							this.followed = true;
						}
					}
				});
			},
			cancelFollow: function() {
				uni.request({
					url: this.apiServer + '/follow/cancel',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						fromUId: this.userId,
						toUId: this.article.uId
					},
					success: res => {
						if (res.data.code === 0) {
							uni.showToast({
								title: '已取消关注'
							});
							this.followed = false;
						}
					}
				});
			},
			like:function(){
				uni.request({
					url: this.apiServer + '/like/add',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						fromLike: this.userId,
						// fromUId: uni.getStorageSync('login_key').userId,
						toLike: this.article.aId
					},
					success: res => {
						if (res.data.code === 0) {
							uni.showToast({
								title: '点赞成功'
							});
							this.liked = true;
						}
					}
				});
			},
			cancelLike: function() {
				uni.request({
					url: this.apiServer + '/like/cancel',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						fromLike: this.userId,
						toLike: this.article.aId
					},
					success: res => {
						if (res.data.code === 0) {
							uni.showToast({
								title: '已取消赞'
							});
							this.liked = false;
						}
					}
				});
			}
		}
	};
</script>

<style>
	.content {
		margin-bottom: 10px;
		margin-top: 10px;
		padding: 5px;
		border-bottom: 1px solid #eee;
	}

	.img-list {
		display: flex;
		flex-direction: column;
	}

	.img-item {
		width: 100%;
		height: 150px;
		margin-bottom: 5px;
	}

	.img-item image {
		width: 100%;
		height: 100%;
		border-radius: 5px;
	}

	.comment-item {
		display: flex;
		/* align-items: center; */
		border-bottom: 1px solid #eee;
		margin-bottom: 10px;
		padding: 5px;
	}

	.comment-item .left {
		flex: 1 1 15%;
	}
	.comment-item .left image{
		height: 40px;
		width: 40px;
	}

	.comment-item .right {
		flex: 1 1 85%;
		display: flex;
		flex-direction: column;
	}
	.comment-item .right .right-name{
		font-weight: 600;
	}
	.comment-item .top{
		color: #999999;
		font-size: 12px;
		margin-top: 5px;
	}

	.comment-box {
		border: 1px solid #fff;
		border-radius: 5px;
		background-color: #eee;
	}

	.follow-btn {
		height: 33px;
		width: 80px;
		font-size: 12pt;
		text-align: center;
		padding-bottom: 20px;
		margin-right: 0px;
		margin-top: 4px;
	}

	.cancel {
		background-color: #aaa;
		margin-top: 4px;
	}
	
	.like-btn{
		width: 80px;
				height: 33px;
				color: rgb(234, 111, 90);
				border-radius: 25px;
				font-size: 14px;
				background-color: #FFF;
				border: 1px solid rgb(234, 111, 90);
				outline: none;
				display: inline-block;
	}
	
	.cancel2{
		width: 80px;
				height: 33px;
				color: #FFF;
				border-radius: 25px;
				font-size: 14px;
				background-color: rgb(234, 111, 90);
				border: 1px solid rgb(234, 111, 90);
				outline: none;
				display: inline-block;
	}
	
	.uni-input.comment-box{
		height: 60px;
		background-color: #F6F6F6;
		width: 100%;
	}
	.article-title{
		font-size: 25px;
		font-weight: 600;
	}
	.article-info{
		margin-top: 10px;
	}
	.article-info-top{
		display: flex;
		justify-content: space-between;
		height: 40px;
		margin-bottom: 10px;
	}
	.info-top-left image{
		height: 100%;
		width: 40px;
	}
	.info-text{
		text-align: right;
		color: #999999;
	}
	.nickname{
		font-size: 20px;
		position: absolute;
        margin-top: 3%;;
		margin-left: 3%;
	}
	.grace-text{
		font-size: 15px;
	}
	.info-time{
		display: flex;
		justify-content: space-between;
	}
</style>
