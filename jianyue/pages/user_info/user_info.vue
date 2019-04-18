<template>
	<view class="container">
		<view class="list">
			<view class="list-item list-item-heigher">
				<navigator url="../modify/modify" class="list-item list-item-heigher">
					<view class="left">昵称</view>
				<view class="right">
					<text>{{ nickname }}</text>
				</view>
				</navigator>
			</view>
			<view class="list-item list-item-heigher">
				<view class="left">头像</view>
				<view class="right">
					<image :src="avatar" class="avatar" @tap="showActionSheet"></image>
				</view>
			</view>
			<view class="list-item list-item-heigher">
				<navigator url="../password/modifypwd">
					<view class="left">修改密码</view>
				</navigator>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			nickname: '',
			avatar:'',
			userId: uni.getStorageSync('login_key').userId
		};
	},
	onLoad() {
	},
	onShow() {
		var _this = this;
		uni.request({
			url: this.apiServer+'/user/'+uni.getStorageSync('login_key').userId,
			method: 'GET',
			header: { 'content-type': 'application/json' },
			success: res => {
				if (res.data.code === 0) {
					console.log(res.data.data.avatar+'————————————');
					_this.avatar = res.data.data.avatar;
					_this.nickname = res.data.data.nickname;
				}
			}
		});
	},
	methods: {
		showActionSheet: function() {
			console.log('show');
			var _this = this;
			uni.showActionSheet({
				itemList: ['拍照', '从相册选择'],
				success: function(res) {
					console.log('选中了第' + (res.tapIndex + 1) + '个按钮');
					//选择的是拍照功能
					if (res.tapIndex == 0) {
						uni.chooseImage({
							count: 1,
							sourceType: ['camera'],
							success: function(res) {
								uni.saveImageToPhotosAlbum({
									filePath: res.tempFilePaths[0],
									success: function() {
										console.log('save success');
										uni.uploadFile({
											url:'http://localhost:8080/api/user/avatar', //仅为示例，非真实的接口地址
											filePath: res.tempFilePaths[0],
											name: 'file',
											formData: {
												userId: _this.userId
											},
											success: uploadFileRes => {
												console.log(uploadFileRes.data);
												_this.avatar = uploadFileRes.data;
											}
										});
									}
								});
							}
						});
					}
					//从相册选择
					if (res.tapIndex == 1) {
						uni.chooseImage({
							count: 1, //默认9
							sizeType: ['original', 'compressed'], 
							sourceType: ['album'], //从相册选择
							success: function(res) {
								console.log(JSON.stringify(res.tempFilePaths));
								uni.uploadFile({
									url:'http://localhost:8080/api/user/avatar', 
									filePath: res.tempFilePaths[0],
									name: 'file',
									formData: {
										userId: _this.userId
									},
									success: uploadFileRes => {
										console.log(uploadFileRes.data);
										_this.avatar = uploadFileRes.data;
									}
								});
							}
						});
					}
				},
				fail: function(res) {
					console.log(res.errMsg);
				}
			});
		}
	}
};
</script>

<style>
	.list-item-heigher {
		height: 80px;
		display: flex;
	}

	.left {
		flex: 1 1 30%;
		font-size: 15px;
	}

	.right {
		flex: 1 1 70%;
		font-size: 15px;
	}
</style>
