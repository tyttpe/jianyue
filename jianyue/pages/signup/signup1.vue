<template>
	<view class="container">
		<view class="sign-box">
			<input class="uni-input left" type="number" placeholder="输入手机号" v-model="mobile" required="required" />
			<button class="green-btn small-btn right" @tap="getVerifyCode">验证码</button>
		</view>
		<input class="uni-input" type="number" placeholder="输入验证码" v-model="verifyCode" required="required" />
		<button @tap="checkCode" class="green-btn">下一步</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				mobile: '',
				verifyCode: '',
				disabled: false
			};
		},
		onLoad() {},
		computed: {

		},
		methods: {
			getVerifyCode: function() {
				var _this = this;
				uni.request({
					url: this.apiServer + '/user/verify',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						mobile: _this.mobile
					},
					success: res => {
						if (res.data.code === 0) {
							uni.showToast({
								title: '验证码已发送'
							});
							_this.disabled = true;
							console.log(_this.disabled);
						} else {
							uni.showModal({
								title: '提示',
								content: res.data.msg
							});
						}
					},

				});
			},
			checkCode: function() {
				var _this = this;
				console.log(_this.verifyCode);
				uni.request({
					url: this.apiServer + '/user/check',
					method: 'POST',
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						mobile: _this.mobile,
						verifyCode: _this.verifyCode
					},
					success: res => {
						console.log(res.data);
						if (res.data.code === 0) {
							uni.navigateTo({
								url: '../password/password?mobile=' + _this.mobile
							});
						} else {
							uni.showModal({
								title: '提示',
								content: res.data.msg
							});
						}
					}
				});
			}
		}
	};
</script>

<style>
	.sign-box {
		display: flex;
		align-items: center;
	}

	.left {
		flex: 1 1 70%;
	}

	.small-btn {
		width: 100px;
		height: 40px;
		font-size: 14px;
	}

	.gray-btn {
		background-color: rgb(232, 231, 227);
	}

	.active {
		background-color: rgb(26, 173, 25);
		color: #fff;
	}
</style>
