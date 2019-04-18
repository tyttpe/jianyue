<template>
	<view class="container">
		<view class="list">
			<view class="list-item">
				文章推送
			</view>
			<view class="list-item">
				消息推送
			</view>
			<view class="list-item">
				<navigator url="../user_info/user_info">
					个人资料
				</navigator>
			</view>
			<view class="list-item">
				清除缓存
			</view>
		</view>
		<button @tap="logout" class="green-btn">退出当前账号</button>
	</view>
</template>

<script>
export default {
	data() {
		return {};
	},
	onLoad() {},
	methods: {
		logout: function() {
			console.log('log out');
			uni.removeStorageSync('login_key');
			uni.showToast({
				title: '已经退出当前账号'
			});
			uni.navigateBack();
		}
	}
};
</script>

<style>
	.list-item{
		font-size: 15px;
	}
</style>