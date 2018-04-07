'use strict';

const TelegramBot = require('node-telegram-bot-api'),
    request = require('request'),
    fs = require('fs'),
    token = '371686725:AAGngbC4U3AtZ2QgtT2UhpMYxNiJEj6K4hQ',
    bot = new TelegramBot(token, {polling: true});
var firstMsg = 0;
var userArray = [];
var isBlock = false;

bot.on('message', function (msg) { //метод message, а в msg само сообщение
    const id = msg.from.id,
        _messageText = msg.text,
        messageText = _messageText.toLowerCase();
    console.log('msg: ', msg);

    var user;
    if (containsUser(id)) {
        user = getUserById(id);
        console.log('user: ', user);
    } else {
        user = {
            userId: id,
            status: false
        }
    }

    if (messageText === 'start notification') {
        user.status = true;
    } else if (messageText === 'stop notification') {
        user.status = false;
    } else if (messageText === 'weather') {

    } else {
        if (containsUser(id)) {
            user.status = getCurrentUserStatus(user);
        }
    }

    if (!containsUser(id)) {
        userArray.push(user);
    } else {
        setNewStatus(user);
    }

    console.log('array: ', userArray);
    console.log('user.status: ', user.status);
    sendExchangeRate();


    if (messageText.indexOf('lab') + 1 || messageText === 'rgr') {
        let file = __dirname + '/dock/' + messageText + '.docx';
        bot.sendDocument(msg.chat.id, file);
    } else if (messageText.indexOf('info') + 1) {
        bot.sendMessage(msg.chat.id, 'file');
    } else {

    }

});

function getCurrentUserStatus(user) {
    for (var i = userArray.length - 1; i >= 0; i--) {
        if (userArray[i].userId === user.userId) {
            return user.status;
        }
    }
}

function getUserById(userId) {
    for (var i = userArray.length - 1; i >= 0; i--) {
        if (userArray[i].userId === userId) {
            return userArray[i];
        }
    }
    return false;
}

function containsUser(userId) {
    for (var i = userArray.length - 1; i >= 0; i--) {
        if (userArray[i].userId === userId) {
            return true;
        }
    }
    return false;
}

function setNewStatus(user) {
    for (var i = userArray.length - 1; i >= 0; i--) {
        if (userArray[i].userId === user.userId) {
            userArray[i].status = user.status;
        }
    }
}

function getFormattedDate() {
    var date = new Date();
    var Year = date.getFullYear();
    var Month = date.getMonth();
    var Day = date.getDate();
    var fMonth;

    // Преобразуем месяца
    switch (Month) {
        case 0:
            fMonth = "января";
            break;
        case 1:
            fMonth = "февраля";
            break;
        case 2:
            fMonth = "марта";
            break;
        case 3:
            fMonth = "апреля";
            break;
        case 4:
            fMonth = "мае";
            break;
        case 5:
            fMonth = "июня";
            break;
        case 6:
            fMonth = "июля";
            break;
        case 7:
            fMonth = "августа";
            break;
        case 8:
            fMonth = "сентября";
            break;
        case 9:
            fMonth = "октября";
            break;
        case 10:
            fMonth = "ноября";
            break;
        case 11:
            fMonth = "декабря";
            break;
    }
    var result = "Сегодня " + Day + " " + fMonth + " " + Year + " года \n";
    console.log(result);
    return result;
}

function sendExchangeRate() {
    console.log('is blosk: ' + isBlock);
    if (!isBlock) {
        isBlock = true;
        setInterval(function () {
            var currentDate = new Date();
            if((currentDate.getHours() == '10'  && currentDate.getMinutes() == '53  ') ||
              (currentDate.getHours() == '12' && currentDate.getMinutes() == '00')){
            console.log('Send rate', currentDate);
            request('https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5', function (error, response, body) {
                if (!error && response.statusCode === 200) {
                    const data = JSON.parse(body);
                    var message = getFormattedDate();
                    data.forEach(function (value, index) {
                        message += `Валюта: ${value.ccy} -> ${value.base_ccy} | Покупка: ${value.buy} | Продажа: ${value.sale} \n`;
                    });
                    /*send message*/
                    for (var i = userArray.length - 1; i >= 0; i--) {
                        if (userArray[i].status) {
                            console.log('message: ' + message);
                            bot.sendMessage(userArray[i].userId, (message));
                        }
                    }
                }
            })

            /* weather api*/
                request('https://api.openweathermap.org/data/2.5/weather?lat=46.48&lon=30.73&APPID=ea4df32bcad15dd978c73c70b40a0935', function (error, response, body) {
                    console.log('weather error:', error);
                    console.log('statusCode:', response.statusCode);
                    if (!error && response.statusCode === 200) {
                        const data = JSON.parse(body);
                        var weather;
                        var descrip;
                        data.weather.forEach(function (value, iterator) {
                            console.log('value: ', value);
                        })
                        weather = `Город: ${data.name} \n Общие сведения:  \n Ветер: ${data.wind.speed} м/с \n Температура: ${Math.round(data.main.temp) - 273} \n Уровень облачности: ${data.clouds.all} \n Давление: ${data.main.pressure} мм.рт.ст`;
                        console.log('weather:', weather);
                        for (var i = userArray.length - 1; i >= 0; i--) {
                            if (userArray[i].status) {
                                bot.sendMessage(userArray[i].userId, (weather));

                            }
                        }
                    }

                })
            }
        }, 60000);
    }

}

