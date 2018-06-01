$(function() {
	/**
	 * get block list
	 */
	$.ajax({
		beforeSend:function (XMLHttpRequest) {
			XMLHttpRequest.setRequestHeader('i-msg-type', lang);
		},
		url:host + '/api/ifood/block',
		type:'get',
		dataType:'json',
		success:function(res){
			var htmlVar;
			if(res.code=='0000' && res.data){
				$.each(res.data, function(i,value){
					htmlVar = htmlVar
						+ "<tr>"
						+ "<td scope='row' class='col-md-1'>"
						+ "<button type='button' class='btn btn-link' onclick=heightDetail("+value.height+")>"
						+ value.height
						+ "</button>"
						+ "</td>"
						+ "<td class='col-md-1'>"
						+ timestampToTime(value.timestamp)
						+ "</td>"
						+ "<td class='col-md-1'>"
						+ value.transactionNum
						+ "</td>"
						+ "<td class='th-right col-md-1'>"
						/*+ value.hash.substr(0,50)+'...'*/
						+ value.hash
						+ "</td>"
						+ "</tr>"
				})
				$("#indexBlockId").html(htmlVar)
			}else {
				console.error("refurbish block info error.")
			}
		},
		error:function () {
			console.log("index block list error")
		}
	});

	/**
	 * get tx list
	 */
	$.ajax({
		beforeSend:function (XMLHttpRequest) {
			XMLHttpRequest.setRequestHeader('i-msg-type', lang);
		},
		url:host + '/api/ifood/tx',
		type:'post',
		data: {
			excludeTxTypes: '0'
		},
		dataType:'json',
		success:function(res){
			var htmlVar;
			if(res.code=='0000' && res.data){
				$.each(res.data, function(i,value){
					htmlVar = htmlVar
						+ "<tr>"
						+ "<th scope='row'>"
						+ "<button type='button' value='"
						+ value.txType + "' "
						+ "onclick=txOnclickByhash('" + value.hash + "') "
						+ "class='btn ";

					if(value.txType == 1) {
						htmlVar += "btn-primary";
					}else if(value.txType == 64) {
						htmlVar += "btn-success";
					}
					else if(value.txType == 128) {
						htmlVar += "btn-info";
					}
					else if(value.txType == 129) {
						htmlVar += "btn-warning";
					}else {
						htmlVar += "btn-default";
					}

					htmlVar = htmlVar + "'>"+value.typeName+"</button>"
						+ "</th>"
						+ "<td>"
						+ "<button type='button' class='btn btn-link' onclick=heightDetail("+value.blockId+")>"+value.blockId+"</button>"
						+ "</td>"
						+ "<td>"+timestampToTime(value.timestamp)+"</td>"
						+ "</tr>"
				})
				$("#indexTxId").html(htmlVar)
			}else {
				console.error("refurbish block info error.")
			}
		},
		error:function () {
			console.log("index tx list error")
		}
	});
})



