<template>
	<view class="content">
		<view class="article-box">
			<view class="article" v-for="(article,index) in articles" :key="index">
				<text class="article-title" @tap="gotoDetail(article.id)">{{article.title}}</text>
				<!-- 大于三张 -->
				<view class="text-box3" v-if="article.imgs.length >=3">
					<view class="thumbnail-box">
						<view class="thumbnail-item" v-for="(item,index2) in article.imgs" :key="index2" v-if="index2<3">
							<image :src="item.imgUrl">
							</image>
						</view>
					</view>
				</view>
				<!-- 小于三张 -->
				<view class="text-box2" v-else-if="article.imgs.length >=1">
					<view class="text-img-box">
						<view class="left">
							<text class="text-box-con">{{handleContent(article.content)}}</text>
						</view>
						<view class="right">
							<image :src="article.imgs[article.imgs.length - 1].imgUrl"></image>
						</view>
					</view>
				</view>
				<!-- 没有图片的显示方式 -->
				<view class="text-box" v-else>
					<text class="text-box-con">{{handleContent(article.content) }}</text>
				</view>
				<!-- 文章作者等信息 -->
				<view class="article-info">
					<view class="article-ava">
						<image :src="article.avatar" class="avatar small"></image>
						<text class="info-text">{{article.nickname}}</text>
						<text class="info-text">{{article.comments}} 评论</text>
					</view>
					<view class="article-nt">
						<text class="info-text">{{handleTime(article.createTime)}}</text>
					</view>
				</view>
			</view>
			<uni-load-more :loadingType="loadingType" :contentText="contentText"></uni-load-more>
		</view>
		<navigator url="../write/write">
			<button type="primary" class="my_btn">+</button>
		</navigator>
	</view>
</template>

<script>
	import uniLoadMore from '../../components/uni-load-more.vue';
	var 
		page = 1,
		timer = null;
	// 定义全局参数,控制数据加载
	export default {
		components: {
			uniLoadMore
		},
		data() {
			return {
				articles: [],
				comments: [],
				loadingText: '加载中...',
				loadingType: 0, //定义加载方式 0---contentdown  1---contentrefresh 2---contentnomore
				contentText: {
					contentdown: '上拉显示更多',
					contentrefresh: '正在加载...',
					contentnomore: '没有更多数据了'
				}
			}
		},
		onLoad: function() {
			this.getArticles();
		},
		onShow: function() {
			this.getArticles();
		},
		onPullDownRefresh: function() {
			this.getArticles();
		},
		onReachBottom: function() {
			var _this = this;
			//触底的时候请求数据，即为上拉加载更多
			//为了更加清楚的看到效果，添加了定时器
			if (timer != null) {
				clearTimeout(timer);
			}
			timer = setTimeout(function() {
				_this.getmorenews();
			}, 5);

			// 正常应为:
			// _self.getmorenews();
		},
		methods: {
			getArticles: function() {
				var _this = this;
				uni.request({
					url: this.apiServer + '/article/list',
					method: 'GET',
					header: {
						'content-type': 'application/x-www.form-urlencoded'
					},
					success: res => {
						_this.articles = res.data.data;
						page++;
					},
					complete: function() {
						uni.stopPullDownRefresh();
					}
				});

			},
			getmorenews: function() {
				var _this = this;
				if (_this.loadingType !== 0) { //loadingType!=0;直接返回
					return false;
				}
				_this.loadingType = 1;
				uni.showNavigationBarLoading(); //显示加载动画
				uni.request({
					url: this.apiServer + '/article/list',
					method: 'GET',
					header: {
						'content-type': 'application/x-www.form-urlencoded'
					},
					data: {
						pageNo: page,
						pageSize: 5
					},
					success: function(res) {
						console.log(JSON.stringify(res));
						if (res.data.data == '') { //没有数据
							_this.loadingType = 2;
							uni.hideNavigationBarLoading(); //关闭加载动画
							return;
						}
						page++; //每触底一次 page +1
						_this.articles = _this.articles.concat(res.data.data); //将数据拼接在一起
						_this.loadingType = 0; //将loadingType归0重置
						uni.hideNavigationBarLoading(); //关闭加载动画
					}
				});
			},
			gotoDetail: function(aId) {
				uni.navigateTo({
					url: '../article_detail/article_detail?aId=' + aId
				});
			},
			handleTime: function(datetime) {
				let date = new Date(datetime);
				let Y = date.getFullYear() + '-';
				let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) + '-' : date.getMonth() + 1 + '-';
				let D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
				let h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
				let m = date.getMinutes() < 10 ? '0' + date.getMinutes() + ':' : date.getMinutes() + ':';
				let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
				return Y + M + D + h + m + s;
			},
			handleContent: function(msg) {
				let description = msg;
				description = description.replace(/(\n)/g, "");
				description = description.replace(/(\t)/g, "");
				description = description.replace(/(\r)/g, "");
				description = description.replace(/<\/?[^>]*>/g, "");
				description = description.replace(/\s*/g, "");
				return description;
			}
		}
	}
</script>

<style>
	.thumbnail-box {
		display: flex;
		height: 110px;
		margin-left: 5px;
		margin-right: 5px;
		padding-bottom: 5px;
	}

	.thumbnail-item {
		flex: 1 1 30%;
		text-align: center;
	}

	.thumbnail-item image {
		height: 100%;
		width: 99%;
		border-radius: 8%;
	}

	.content {
		background-color: #F2F3F4;
	}

	.article {
		display: flex;
		flex-direction: column;
		width: 100%;
		margin-bottom: 8px;
		background-color: rgb(255, 255, 255);
		height: 170px;
	}

	.article-title {
		margin-bottom: 10upx;
		margin-left: 5px;
		margin-right: 5px;
		font-weight: 400;
		font-size: 22px;
	}

	.text-box {
		width: 100%;
		height: 110px;
		color: #333333;
	}

	.text-box-con {
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 4;
		overflow: hidden;
		margin-right: 5px;
		font-size: 15px;
	}

	.text-box2 {
		width: 100%;
		color: #333333;
		margin-bottom: 10upx;
	}

	.text-box3 {}

	.text-img-box {
		display: flex;
		flex-direction: row;
		margin-left: 5px;
		margin-right: 5px;
	}

	.left {
		flex: 1 1 60%;
		width: 100%;
		height: 100%;
	}

	.right {
		flex: 1 1 40%;
		width: 100%;
		height: 100%;
	}

	.right image {
		width: 100%;
		height: 250upx;
		border-radius: 8%;
	}

	.article-info {
		height: 60upx;
		color: #E9E9E9;
		margin: 0px 5px auto;
		display: flex;
		justify-content: space-between;
	}

	.info-text {
		color: #999999;
		font-size: 14px;
		margin-right: 10px;
	}

	.article-info image {
		height: 80%;
		width: 20%;
		margin-right: 10px;
	}

	.article-ava {
		flex: 1 1 45%;
		display: flex;
	}

	.article-nt {
		flex: 1 1 55%;
		text-align: right;
	}

	.my_btn {
		/* background: linear-gradient(bottom, blue, #fff);
		background: -ms-linear-gradient(bottom, blue, #fff);
		background: -webkit-linear-gradient(bottom, blue, #fff);
		background: -o-linear-gradient(bottom, blue, #fff);
		background: -moz-linear-gradient(bottom, blue, #fff); */

		border-radius: 50%;
		background-color: #de533a;
		background: linear-gradient(40deg, #ffd86f, #fc6262);

		border-radius: 25px;
		width: 15%;

		position: fixed;
		top: 82%;
		right: 2%;

		box-shadow: 0px 2px 10px #000;
	}
</style>
