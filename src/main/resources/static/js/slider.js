let sliderStart = false;
let x, y; // 鼠标最初位置
let back_width = 300;
let mask_width = 50;
let lx, ly;//扣出图的左上角坐标
let yArray;

$(function () {
    // 初始化验证码
    $.ajax({
        type: "get",
        url: "http://localhost:8080/verification",
        dataType: "json",
        async: false,

        success: function (data) {
            console.log("获取后端数据");
            lx = data.data.xlocation;
            ly = data.data.ylocation;
            initImg(data.data.backName, data.data.markName, ly);
            initMovement();
        },
        error: function () {
            alert("error")
        }
    });
});

// 拼图验证码用---计算和
function sum(x1, x2) {
    return x1 + x2
}

// 拼图验证码用---初始化图片
function initImg(backgroundImg, markImg, yLocation) {
    $('#back_img').attr('src', "data:image/png;base64," + backgroundImg);
    $('#before_img').attr('src', "data:image/png;base64," + markImg);
    $('.slide_img_mark').css('margin-top', yLocation);
}

// 拼图验证码用---设置回调
function initMovement() {
    $('.slider_arrow')[0].addEventListener('mousedown', sliderPush);
    window.addEventListener('mousemove', sliderDrug);
    window.addEventListener('mouseup', sliderEnd);
}

// 拼图验证码用---按下滑动按钮回调方法
function sliderPush(e) {
    sliderStart = true
    x = e.clientX || e.touches[0].clientX;
    y = e.clientY || e.touches[0].clientY;
    $('.slider_tip').hide();
    $('.slider_mask').css('background-color', '#deee97');
    $('#arrow_icon').prop('src', '/icon/right_arrow.png');
    yArray = []
}

// 拼图验证码用---开始滑动回调方法， 改变arrow位置和mask大小
function sliderDrug(e) {
    if (!sliderStart)
        return false;

    // 获取鼠标移动位置
    const eventX = e.clientX || e.touches[0].clientX;
    const eventY = e.clientY || e.touches[0].clientY;
    const moveX = eventX - x;
    const moveY = eventY - y;

    // 存放y轴坐标，便于后续判断
    yArray.push(moveY);

    // 确保边界
    if (moveX < 0 || moveX + parseInt(mask_width) > back_width)
        return false;

    // 改变slider_arrow的位置和mask大小
    $('.slider_arrow').css('margin-left', moveX);
    $('.slider_mask').css('width', moveX);
    $('.slide_img_mark').css('margin-left', moveX);
}

// 拼图验证码用---结束时进行判断回调方法
function sliderEnd(e) {
    if (!sliderStart)
        return false;

    sliderStart = false;
    finalX = $('.slide_img_mark').css('margin-left');
    // 5像素的误差
    finalX = finalX.toString().slice(0, -2);
    if (finalX < (lx - 5) || finalX > (lx + 5)) {
        failed()
    } else if (yVerify()) {
        console.log("verify success");
        success()
    } else {
        console.log("verify failed");
        failed()
    }
}

// 拼图验证码用---检测y轴变化
function yVerify() {
    if (yArray.length < 1)
        return false;
    let sumY = yArray.reduce(sum);
    let average = sumY / yArray.length;
    // 简单看看，y没变过算不对劲
    let same = true;
    for (let i = 0; i < yArray.length; i++) {
        if (yArray[i] != average)
            same = false
    }
    return !same
}

// 拼图验证码用---验证成功回调方法
function success() {
    console.log("success");

    $('#arrow_icon').prop('src', '/icon/green_correct.png');
    $('.slider_mask').css('background-color', '#79e77e');

    alert("验证成功");

    // function successLink(){
    //     window.open('/index')
    // }
    //
    // setTimeout(successLink, 1000)
}

// 拼图验证码用---失败重置
function failed() {
    console.log("failed");

    $('#arrow_icon').prop('src', '/icon/red_error.png');
    $('.slider_mask').css('background-color', '#e73c4a');

    alert("验证失败");

    setTimeout(reset, 500)
}

// 拼图验证码用---修改位置和重置颜色
function reset() {
    $('.slider_mask').css('background-color', '#deee97');

    $('.slider_arrow').css('margin-left', 0);
    $('.slider_mask').css('width', 0);
    $('.slide_img_mark').css('margin-left', 0);

    $('#arrow_icon').prop('src', '/icon/right_arrow.png');
    $('.slider_tip').show()
}
