<template>
	<div>
		<input type="text" v-model="nickname" class="nickname" placeholder=" 输入昵称">
		<button type="primary" @tap="Save(nickname)" class="btn">保存</button>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				nickname: ""
			}
		},
		onLoad() {

		},
		onShow() {
			
		},
		methods: {
         Save:function(nickname){
					 
			var _this = this;
			uni.request({
				url: this.apiServer+'/user/update?id='+uni.getStorageSync('login_key').userId,
				
				method: 'put',
				data:nickname,
				header: { 'content-type': 'application/json' },
				success: res => {
					uni.navigateBack();
				}
			});
		
				 }
		}
	}
</script>

<style>
	.nickname{
		margin: 20px 15px auto;
		height: 30px;
	}
	.btn{
		margin-top: 20px;
		width: 90%;
	}
</style>
