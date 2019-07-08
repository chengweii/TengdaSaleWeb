var loadingModal = {
    show: function (loadText) {
        if ($('#loadingModal').attr("show") == true) {
            return;
        }
        $('#loadingModal').modal({backdrop: 'static', keyboard: false});
        $('#loadingModal').attr("show", true);
    }, hide: function () {
        if ($('#loadingModal').attr("show") == false) {
            return;
        }
        $('#loadingModal').modal('hide');
        $('#loadingModal').attr("show", false);
    }
};

function renderList(listConfig) {
    loadingModal.show();
    $.ajax({
        type: "post",
        url: listConfig.url,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(listConfig.params),
        dataType: 'json',
        cache: false,
        success: function (result) {
            if (result && result.code == 200) {
                $(".container").html("");

                var html = "";
                html += "<h3><a href=\"javaScript:void(0);\" type=\"button\" class=\"btn btn-primary btn-sm btn-add\">添加</a></h3>";

                if (!result || !result.result) {
                    html += "<div class=\"alert alert-warning\" role=\"alert\"><span class=\"glyphicon glyphicon-info-sign\" aria-hidden=\"true\"></span> 没有查询到数据，请添加。</div>";
                    $(".container").html(html);
                    loadingModal.hide();
                    return;
                }

                var data = result.result.content;

                var table = $("<table class=\"table table-bordered table-striped\"></table>");
                var th = $("<tr></tr>");
                for (var index in listConfig.headers) {
                    th.append("<th>" + listConfig.headers[index] + "</th>");
                }
                th.append("<th>操作</th>");
                table.append(th);

                for (var index in data) {
                    var tr = $("<tr></tr>");
                    for (var indexC in listConfig.attrNames) {
                        tr.append("<td class='" + listConfig.attrNames[indexC] + "' item-name='" + listConfig.attrNames[indexC] + "'>" + data[index][listConfig.attrNames[indexC]] + "</td>");
                    }
                    var tdo = $("<td></td>");
                    tdo.append(" <a href=\"javaScript:void(0);\" type=\"button\" class=\"btn btn-sm btn-success btn-modify\" item-id=\"" + data[index]["id"] + "\" item-state='normal'> 修改</a>");
                    tdo.append(" <a href=\"javaScript:void(0);\" type=\"button\" class=\"btn btn-sm btn-danger btn-remove\" item-id=\"" + data[index]["id"] + "\"> 删除</a>");
                    tr.append(tdo);
                    table.append(tr);
                }

                $(".container").append(html);
                $(".container").append(table);

                $(".container").append("<div class=\"paginator\" style=\"text-align: center\"></div>");
                $(".paginator").pagination({
                    currentPage: result.result.number + 1 || 1,
                    displayedPages: 5,
                    pages: result.result.totalPages,
                    itemsOnPage: result.result.numberOfElements,
                    items: result.result.totalElements,
                    cssStyle: 'light-theme',
                    onPageClick: function (pageNumber, event) {
                        listConfig.params.pageNo = pageNumber;
                        renderList(listConfig);
                    }
                });
                $(".paginator .prev").html("上一页");
                $(".paginator .next").html("下一页");

                if (listConfig.callback) {
                    listConfig.callback(result);
                }

                loadingModal.hide();
                console.log("renderList finished.");
            } else {
                console.log("renderList failed.");
            }
        }
    });
}

function renderAdd(addConfig, listConfig) {
    $(".btn-add").click(function () {
        loadingModal.show();
        if (!addConfig || !addConfig.headers) {
            return;
        }
        var addForm = $("<div style='margin-top:20px;'></div>");
        for (var index in addConfig.headers) {
            addForm.append("<div class=\"form-group\"><label for=\"title\">" + addConfig.headers[index] + ":</label><input type=\"text\" class=\"form-control\" name=\"" + addConfig.attrNames[index] + "\" name=\"title\" placeholder=\"请输入\"/></div>");
        }
        addForm.append("<div class=\"form-group\"><a href=\"javaScript:void(0);\" type=\"button\" class=\"btn btn-sm btn-danger btn-save\" >提交</a></div>");
        $(".container").html(addForm);

        $(".btn-save").click(function(){
            loadingModal.show();
            $.ajax({
                type: "post",
                contentType: "application/json;charset=utf-8",
                url: addConfig.url,
                data: removeConfig.params,
                dataType: 'json',
                cache: false,
                success: function (result) {
                    if (!result || result.code != 200) {
                        toastr.error('删除失败');
                    } else {
                        toastr.success('删除成功');
                    }
                    listConfig.params.pageNo = 1;
                    renderList(listConfig);
                    loadingModal.hide();
                }
            });
        });

        loadingModal.hide();
    });

    console.log("renderAdd finished.");
}

function renderRemove(removeConfig, listConfig) {
    $(".btn-remove").click(function () {
        var item = $(this);
        loadingModal.show();
        $.ajax({
            type: "post",
            url: removeConfig.url + "/" + item.attr("item-id"),
            data: removeConfig.params,
            dataType: 'json',
            cache: false,
            success: function (result) {
                if (!result || result.code != 200) {
                    toastr.error('删除失败');
                } else {
                    toastr.success('删除成功');
                }
                listConfig.params.pageNo = 1;
                renderList(listConfig);
                loadingModal.hide();
            }
        });
    });
    console.log("renderRemove finished.");
}

function renderModify(modifyConfig, listConfig) {
    $(".btn-modify").click(function () {
        var item_state = $(this).attr("item-state");
        if (item_state == 'normal') {
            if (modifyConfig && modifyConfig.attrNames) {
                var row = $(this).parent().parent();
                var tds = row.find("td");
                for (var index in modifyConfig.attrNames) {
                    $.each(tds, function () {
                        if ($(this).attr("item-name") == modifyConfig.attrNames[index]) {
                            var input = $("<input type=\"text\" class=\"form-control\" name='" + $(this).attr("item-name") + "' placeholder=\"请输入\" style='width:140px'/>");
                            input.val($(this).text());
                            $(this).html(input);
                        }
                    });
                }
                $(this).html("保存");
            }
            $(this).attr("item-state", "editing");
        } else {
            if (modifyConfig && modifyConfig.attrNames) {
                loadingModal.show();

                var row = $(this).parent().parent();
                var tds = row.find("td");
                var params = {id: $(this).attr("item-id")};
                $.each(tds, function () {
                    var input = $(this).find("input");
                    if (input[0]) {
                        params[$(this).attr("item-name")] = input.val();
                    }
                });

                $.ajax({
                    type: "post",
                    contentType: "application/json;charset=utf-8",
                    url: modifyConfig.url,
                    data: JSON.stringify(params),
                    dataType: 'json',
                    cache: false,
                    success: function (result) {
                        if (!result || result.code != 200) {
                            toastr.error('修改失败');
                        } else {
                            toastr.success('修改成功');
                        }
                        renderList(listConfig);
                        loadingModal.hide();
                    }
                });
            }
            $(this).html("修改");
            $(this).attr("item-state", "normal");
        }
    });
}

function renderModule(listConfig, removeConfig, modifyConfig, addConfig) {
    listConfig.callback = function () {
        renderRemove(removeConfig, listConfig);
        renderModify(modifyConfig, listConfig);
        renderAdd(addConfig, listConfig);
    };
    renderList(listConfig);
}