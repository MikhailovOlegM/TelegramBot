'use strict';

const TelegramBot = require('node-telegram-bot-api'),
      request = require('request'),
      fs = require('fs'),
      token = '371686725:AAGngbC4U3AtZ2QgtT2UhpMYxNiJEj6K4hQ',
      bot = new TelegramBot(token, {polling:true});
      var firstMsg = 0;

bot.on('message', function(msg){ //метод message, а в msg само сообщение
    const id = msg.from.id,
	  _messageText = msg.text,
	  messageText = _messageText.toLowerCase();


    if (firstMsg == 0 || messageText === 'привет') {
      bot.sendMessage(id, 'Привет, я бот Олега' + '\r\n'
      + 'Если ты хочешь получить лабораторную работу, то напиши мне её номер в формате' + '\r\n'
    + 'lab#&' + '\r\n' + 'где # - номер работы от 5 до 11 включительно' + '\r\n'
  + '& - вариант на Java или С++, пишете j или c, например, lab6c');
    firstMsg = 1;
  }else if (messageText === 'курс'){
       request('https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5', function(error, response, body){
	 if(!error && response.statusCode === 200){
	 const data = JSON.parse(body);
	 data.forEach(function(value, index) {
		bot.sendMessage(id, (`Валюта: ${value.ccy} | Код национальной валюты: ${value.base_ccy} | Покупка: ${value.buy}`))
	   });
	 }
   })
 } else if (messageText.indexOf('lab')+1 || messageText==='rgr') {
   let file = __dirname + '/dock/' + messageText + '.docx';
   bot.sendDocument(id, file);
 } else {
    bot.sendMessage(id, 'Я не знаю такой команды');
  }

});
