<template>
	<view class="container">
		<!-- 顶部头像和昵称区域，纵向排列 -->
		<view class="top">
			<view class="avatar-box">
				<image src="../../static/default.png" mode="scaleToFill" class="avatar" v-if="!storageData.login"></image>
				<image :src="avatar" mode="scaleToFill" class="avatar" v-if="storageData.login"></image>
			</view>
			<view class="info-box">
				<navigator url="../signin/signin" v-if="!storageData.login">点击登录</navigator>
				<text v-if="storageData.login">{{ nickname }}</text>
				<navigator url="../setting/setting" v-if="storageData.login"><text class="setting-txt">个人设置</text></navigator>
			</view>
		</view>

		<view class="cen">
			<uni-segmented-control :current="current" :values="items" @clickItem="onClickItem" style-type="button" active-color="#4cd964">
			</uni-segmented-control>
			<view class="content">
				<view v-show="current === 0" class="select">
					<view class="list-item" v-for="(article, index) in articles" :key="index">
						<uni-swipe-action @tap="bindClick(article.id,index)" :options="options">
							<view class='cont'>
								<text @tap="gotoDetail(article.id)">{{ article.title }}</text>

							</view>
						</uni-swipe-action>
					</view>
				</view>
				<view v-show="current === 1" class="select">
					<view class="list-item" v-for="(follow, index) in follows" :key="index">
						<image :src="follow.avatar" class="avatar small"></image>
						<text style="margin-left: 20px;" @tap="gotoother(follow.toUId)">{{ follow.nickname }}</text>
					</view>

				</view>
				<view v-show="current === 2" class="select">
					<view class="list-item" v-for="(like, index) in likes" :key="index">
						<text @tap="gotoDetail(like.toLike)">{{ like.title }}</text>
					</view>
				</view>
				<view v-show="current === 3" class="select">
					
				</view>
			</view>
		</view>


	</view>
</template>

<script>
	var loginRes, _self;
	import uniSegmentedControl from "../../components/uni-segmented-control.vue"
	import uniSwipeAction from "../../components/uni-swipe-action.vue"
	export default {
		components: {
			uniSegmentedControl,
			uniSwipeAction
		},
		data() {
			return {
				storageData: {},
				avatar: '',
				nickname: '',
				articles: [],
				follows: [],
				likes: [],
				items: ['文章', '关注', '收藏', '积分'],
				current: 0,
				options: [{
					text: '删除',
					style: {
						backgroundColor: '#dd524d'
					}
				}]
			};
		},
		onLoad: function() {},
		onPullDownRefresh: function() {
			this.bindClick();
		},
		onShow: function() {
			var _this = this;
			const loginKey = uni.getStorageSync('login_key');
			if (loginKey) {
				// console.log(loginKey);
				this.storageData = {
					login: loginKey.login,
					nickname: loginKey.nickname,
					avatar: loginKey.avatar,
					userId: loginKey.userId
				};
			} else {
				this.storageData = {
					login: false
				};
			}
			uni.request({
				url: this.apiServer + '/user/' + uni.getStorageSync('login_key').userId,
				method: 'GET',
				header: {
					'content-type': 'application/json'
				},
				success: res => {
					if (res.data.code === 0) {
						console.log(res.data.data.avatar + '————————————');
						_this.avatar = res.data.data.avatar;
						_this.nickname = res.data.data.nickname;
					}
				}
			});
			uni.request({
				url: this.apiServer + '/article/user',
				method: 'GET',
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				data: {
					userId: this.storageData.userId
				},
				success: res => {
					_this.articles = res.data.data;
				}
			});
			uni.request({
				url: this.apiServer + '/follow/list',
				method: 'GET',
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				data: {
					fromUId: this.storageData.userId
				},
				success: res => {
					_this.follows = res.data.data;
				}
			});
			uni.request({
				url: this.apiServer + '/like/list',
				method: 'GET',
				header: {
					'content-type': 'application/x-www-form-urlencoded'
				},
				data: {
					fromLike: this.storageData.userId
				},
				success: res => {
					_this.likes = res.data.data;
				}
			});
		},
		methods: {
			onClickItem(index) {
				if (this.current !== index) {
					this.current = index;
				}
			},
			bindClick: function(aId,index) {
				uni.request({
					url: this.apiServer + '/article/delete',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						uId: this.storageData.userId,
						id: aId
					},
					success: res => {
						if (res.data.code === 0) {
							uni.showToast({
								title: '删除文章'
							});
							this.articles.splice(index,1)
						}
					}
				});
			},
			gotoDetail: function(aId) {
				uni.navigateTo({
					url: '../article_detail/article_detail?aId=' + aId + '&userId=' + this.storageData.userId
				});
			},
			gotoother: function(uId) {
				uni.navigateTo({
					url: '../other/other?uId=' + uId
				})
			}
		}
	};
</script>

<style scoped>
	.top {
		display: flex;
		flex-direction: column;
		text-align: center;
		height: 100px;
		margin-top: 5px;
	}

	.cen {
		margin: 0 15px auto;
	}

	.avatar-box {
		flex: 1 1 30%;
	}

	.info-box {
		flex: 1 1 70%;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 15px;
	}

	.setting-txt {
		color: #00b26a;
		margin-left: 15px;
	}

	.center {
		display: flex;
		justify-content: center;
	}

	.info {
		flex: 1 1 25%;
		display: flex;
		flex-direction: column;
		text-align: center;
		border-right: 1px solid #eee;
	}

	.title {
		font-size: 14pt;
	}

	.content {
		margin-top: 20px;
	}

	.list-item image {
		height: 80%;
		width: 20%;
	}

	uni-view {
		font-size: 15px;
	}
</style>
