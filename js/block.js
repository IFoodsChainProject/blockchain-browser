var currenttime;
$(function() {
	currenttime = getQueryString('currenttime')
	var pageNumber = getQueryString('pageNumber')
	blockList(pageNumber ? pageNumber : 1, currenttime)

})

/**
 * get block list
 */
function blockList(pageNumber, timestramp) {
	$.ajax({
		beforeSend:function (XMLHttpRequest) {
			XMLHttpRequest.setRequestHeader('i-msg-type', lang);
		},
		url:host + '/api/ifood/block',
		type:'post',
		data: {
			'pageNumber': pageNumber,
			'currenttime': timestramp
		},
		dataType:'json',
		success:function(res){
			var htmlVar = "";
			if(res.code=='0000' && res.data){
				htmlVar = htmlVar
					+ "<input type='hidden' name='currentpage' value='"+pageNumber+"'/>"
					/*+ "<input type='hidden' name='totalnum' value='"+res.data[0].totalNum+"'/>"*/
				$.each(res.data, function(i,value){
					htmlVar = htmlVar
						+ "<input type='hidden' name='totalnum' value='"+value.totalNum+"'/>"
						+ "<tr class='row-center'>"
						+ "<th scope='row' class='col-md-1'>"
						+ "<button type='button' class='btn btn-link' onclick=heightDetail("+value.height+")>"
						+ value.height
						+ "</th>"
						+ "<td class='col-md-1'>"
						+ timestampToTime(value.timestamp)
						+ "</td>"
						+ "<td class='col-md-1'>"
						+ value.transactionNum
						+ "</td>"
						+ "<td class='th-right col-md-1'>"
						+ value.hash
						+ "</td>"
						+ "</tr>"
				})

				$("#pageBlockId").html(htmlVar)

				var totaoNum = $("input[name='totalnum']").val() ? $("input[name='totalnum']").val() : 20;
				var currentPage = $("input[name='currentpage']").val() ? $("input[name='currentpage']").val() : 1;
				var element = $('#paginationBlockId');
				var options = {
					bootstrapMajorVersion: 3,
					currentPage: currentPage,
					numberOfPages: 7,
					totalPages: totaoNum/20+1,
					size: "large",
					alignment: "Center",
					itemTexts: function (type, page, current) {
						switch (type) {
							case "first":
								return "<<";
							case "prev":
								return "<";
							case "next":
								return ">";
							case "last":
								return ">>";
							case "page":
								return page;
						}
					}, onPageClicked: function (event, originalEvent, type, page) {
						window.location.href="block.html?currenttime="+currenttime+"&pageNumber="+page;
					},
				};
				element.bootstrapPaginator(options);

			}else {
				console.error("refurbish block info error.")
			}
		},
		error:function () {
			console.log("查询失败")
		}
	});
}
