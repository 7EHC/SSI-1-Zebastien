const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
   specPattern: "cypress/e2e/**/*.{cy,spec}.{js,jsx,ts,tsx}",
  //  baseUrl:"http://localhost:5173/",
  //  baseUrl:"http://ip22ssi1.sit.kmutt.ac.th",
  baseUrl:"http://intproj22.sit.kmutt.ac.th/ssi1",
   experimentalSessionAndOrigin: true
  },
});