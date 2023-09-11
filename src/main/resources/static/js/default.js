const timeLabels = ["09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"];

var step = 0;
var firstId = -1;
var seconId = -1;
var allowCollision = false;
var interfalFilled = false;


let FREE_COLOR = "rgb(135, 216, 135)";
let BOOKING_COLOR = "#ba8ff2";
let RESERVED_COLOR = "lightgray";


$(function () {

  $.datepicker.setDefaults({
    firstDay: 1,
    beforeShowDay: $.datepicker.noWeekends
  });

  $("#datepicker").datepicker({
    dateFormat: "yy-mm-dd",
  });


  $("#datepicker").datepicker()
    .on("change", function (event) {
      console.log("Got change event from field " + getSelectedDate());
      getBookings(getSelectedDate());
    });

  createCalendar();
}); FREE_COLORBOOKING_COLOR
  return $('#datepicker').datepicker({ dateFormat: 'yy-mm-dd' }).val();
}

function validate() {
  var errors = [];
  var clientName = $("#clientName").val();
  var dateField = getSelectedDate();
  if (clientName == null || clientName == "") errors.push("Name is empty");
  if (dateField == null || dateField == "") errors.push("Date is empty");


  if (firstId < 0 || secondId < 0) {
    errors.push("Please select time interval");
  } else {
    if (secondId - firstId > 5) {
      errors.push("Time interval should be maximum 3 hours");
    }
  }

  if (errors.length == 0) {
    return true;
  } else {
    showErrors(errors);
    return false;
  }
}

function sendBooking() {
  if (validate() == false) return;
  console.log("sendBooking");
  var clientName = $("#clientName").val();
  console.log("clientName=" + clientName);
  var date = getSelectedDate();
  addBooking(clientName, date, timeLabels[firstId], timeLabels[secondId + 1])

}



function addBooking(clientName, date, timeFrom, timeTo) {
  $.ajax({
    contentType: 'application/json',
    data: JSON.stringify(
      {
        "name": clientName,
        "date": date,
        "timeFrom": timeFrom,
        "timeTo": timeTo
      })
    ,
    dataType: 'json',
    success: function (data) {
      getBookings(getSelectedDate());
      //alert("ok");
    },
    error: function (e) {
      var result = JSON.parse(e.responseText);
      showErrors(result.errors);
    },
    processData: false,
    type: 'POST',
    url: '/meeting'
  });
}


function showErrors(errors) {
  $("#resultMessage").html("");
  let text = "<ul>";
  for (let i = 0; i < errors.length; i++) {
    text += "<li>" + errors[i] + "</li>";
  }
  text += "</ul>";
  $("#resultMessage").html(text);RESERVED_COLOR
  clearCalendar();
  if (dateString == null || dateString == "") return;
  $.ajax({
    success: function (data) {
      for (var i = 0; i < data.length; i++) {
        fillReserved(data[i])
      }

    },
    error: function (e) {
      var result = JSON.parse(e.responseText);

      let text = "<ul>";
      for (let i = 0; i < result.errors.length; i++) {
        text += "<li>" + result.errors[i] + "</li>";
      }
      text += "</ul>";

      $("#resultMessage").html(text);
    },
    processData: false,
    type: 'GET',
    url: '/meetingByDay/' + dateString
  });
}



function createCalendar() {
  const calendarContainer = document.querySelector('#calendarContainer');
  for (var i = 0; i < timeLabels.length - 1; i++) {
    const cell = document.createElement("div");
    cell.className = "cell";
    cell.id = "cell" + i;
    cell.innerText = timeLabels[i];
    cell.addEventListener('click', function onClick(event) {


      var color = window.getComputedStyle(event.target, null).getPropertyValue('background-color');
      console.log("color=" + color);
      if (allowCollision == false && color != FREE_COLOR) return;
      event.target.style.backgroundColor = BOOKING_COLOR;
      if (step == 0) {
        if (interfalFilled) {
          getBookings(getSelectedDate());
          interfalFilled = false;
          return;
        }
        firstId = parseInt(event.target.id.substring(4));
        console.log("firstId=" + firstId);
        step++;
      } else if (step == 1) {
        secondId = parseInt(event.target.id.substring(4));
        console.log("secondId=" + secondId);
        fillInterval(firstId, secondId, BOOKING_COLOR);
        interfalFilled = true;
        step = 0;
      }
    });
    calendarContainer.appendChild(cell);
  }


}

function fillInterval(from, to, color, name) {
  console.log("fillInterval " + from + " " + to + " color");
  for (var i = from; i <= to; i++) {
    var cell = document.getElementById("cell" + i);
    cell.style.backgroundColor = color;
    if (name != null) cell.setAttribute("title", name);
  }
}

function fillReserved(meeting) {
  let timeFrom = meeting.timeFrom.substring(11, 16);
  console.log("timeFrom=" + timeFrom);
  let timeTo = meeting.timeTo.substring(11, 16);
  console.log("timeTo=" + timeTo);
  let indexFrom = findIndex(timeFrom);
  let indexTo = findIndex(timeTo);
  fillInterval(indexFrom, indexTo - 1, RESERVED_COLOR, meeting.name);
}

function findIndex(timeString) {
  for (var i = 0; i <= timeLabels.length; i++) {
    if (timeLabels[i] == timeString) return i;
  }
  return -1;
}

function clearCalendar() {
  fillInterval(0, 15, FREE_COLOR, null);
  step = 0;
  interfalFilled = false;
  firstId = -1;
  seconId = -1;
  $("#resultMessage").html("");
}
