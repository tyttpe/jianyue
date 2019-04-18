<template>
	<view class="container">
		<input class="uni-input" password type="text" placeholder="输入密码" v-model="password" required="required" />
		<button class="green-btn" @tap="signUp(userDTO)">注册</button>
	</view>
</template>

<script>
export default {
	data() {
		return {
			userDTO: {
				mobile: '',
				password: ''
			}
		};
	},
	onLoad: function(option) {
		//option为object类型，会序列化上个页面传递的参数
		console.log(option.mobile);
		this.mobile = option.mobile;
	},
	methods: {
		signUp: function(userDTO) {
			var _this = this;
			uni.request({
				url: this.apiServer + '/user/sign_up',
				method: 'POST',
				header: { 'content-type': 'application/json' },
				data: {
					mobile: _this.mobile,
					password: _this.password
				},
				success: res => {
					console.log(res.data.code);
					uni.showModal({
						title: '提示',
						content: '注册成功'
					});
					if (res.data.code === 0) {
						uni.navigateTo({
							url: '../signin/signin'
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

<style></style>