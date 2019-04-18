<template>
	<div>
		<input type="password" v-model="oldpassword" class="pwd" placeholder=" 输入原密码">
		<input type="password" v-model="newpassword" class="pwd" placeholder=" 输入新密码">
		<button type="primary" @tap="Save(oldpassword,newpassword)" class="btn">修改</button>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				oldpassword: "",
				newpassword: ""
			}
		},
		onLoad() {

		},
		onShow() {

		},
		methods: {
			Save: function(oldpassword, newpassword) {
				var _this = this;
				/* uni.request({
					url: this.apiServer + '/user/' + uni.getStorageSync('login_key').userId,
					method: 'GET',
					header: {
						'content-type': 'application/json'
					},
					success: res => {
						if (res.data.code === 0) {
							_this.oldpassword = res.data.data.password
								
							

						}
					}
				}); */
				if(oldpassword == newpassword){
					uni.showToast({
						title: '重复密码'
					});
				}else{
					uni.request({
									url: this.apiServer + '/user/updatepwd?id=' + uni.getStorageSync('login_key').userId,
									method: 'put',
									data: newpassword,
									header: {
										'content-type': 'application/json'
									},
									success: res => {
										uni.navigateBack();
									}
								});
				}
				
				
				


			}
		}
	}
</script>

<style>
	.pwd {
		margin: 20px 15px auto;
		height: 30px;
	}

	.btn {
		margin-top: 20px;
		width: 90%;
	}
</style>
