var projectName = ""

/**
 * 时间戳转换
 * @param timestamp
 * @returns {*}
 */
function timestampToTime(timestamp) {
	var date = new Date(timestamp * 1000);
	Y = date.getFullYear() + '-';
	M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	D = (date.getDate() < 10 ? '0'+date.getDate() : date.getDate()) + ' ';
	h = (date.getHours() < 10 ? '0'+date.getHours() : date.getHours()) + ':';
	m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes()) + ':';
	s = date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds();
	return Y+M+D+h+m+s;
}

/**
 * block height deatail info
 * @param height
 */
function heightDetail(height) {
	window.location.href = '/html/block-info.html?height=' + height;
}


/**
 *
 */
function getQueryString(name) {
	/*var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]);
	return null;*/
	paramValue = "", isFound = !1;
	if (this.location.search.indexOf("?") == 0 && this.location.search.indexOf("=") > 1) {
		arrSource = unescape(this.location.search).substring(1, this.location.search.length).split("&"), i = 0;
		while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == name.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++
	}
	return paramValue == "" && (paramValue = null), paramValue
}

function txOnclickByhash(txhash) {
	window.location.href = '/html/tx-info.html?hash=' + txhash;
}
