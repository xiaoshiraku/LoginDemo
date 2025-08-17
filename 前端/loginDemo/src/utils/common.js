//获取当前系统时间
export function getCurrentTime() {
       var now =new Date();
       var year = now.getFullYear();
       var month = now.getMonth() + 1; // 月份从0开始计数
       var day = now.getDate();
       month = month < 10 ? '0' + month : month; // 补零
       day = day < 10 ? '0' + day : day; // 补零
       return year + '-' + month + '-' + day;
    }

//向sessionStorage中存储数据
export function setSessionStorage(key, value) {
   sessionStorage.setItem(key, JSON.stringify(value));

}    

//从sessionStorage中获取数据
export function getSessionStorage(key) {
   var str = sessionStorage.getItem(key);
   if (str == ''|| str == null || str == undefined || str == 'null') {
       return null;
   } else{
       return JSON.parse(str);
   }
}


//从sessionStorage中删除数据
export function removeSessionStorage(key) {
   sessionStorage.removeItem(key);
}

//向localStorage中存储数据
export function setLocalStorage(key, value) {
   localStorage.setItem(key, JSON.stringify(value));
}

//从localStorage中获取数据
export function getLocalStorage(key) {
   var str = localStorage.getItem(key);
   if (str == '' || str == null || str == undefined || str == 'null') {
       return null;
   } else {
       return JSON.parse(str);
   }
}

//从localStorage中删除数据
export function removeLocalStorage(key) {
   localStorage.removeItem(key);
}