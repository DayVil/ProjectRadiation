# Report

I started with looking for a fitting API to make requests for needed UVi data. For this, I chose [OpenWeather](https://openweathermap.org/), specifically the [One Call API](https://openweathermap.org/api/one-call-api). This allowed me to harvest the UVi for any given position.

To request the data, I used [OkHttp3](https://square.github.io/okhttp/) in order to make it simpler. The rest was solved with the built-in JSON library.
