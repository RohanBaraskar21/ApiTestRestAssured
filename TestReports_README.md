# Test Reports Output

## ExtentReports
- Location: `target/ExtentReports/ExtentReport.html`
- How to view: Open the file in your browser and review test results, logs, and details for each test case.

## Allure Report
- Allure results are generated in: `target/allure-results`
- To generate and view the Allure HTML report inside the `target` folder:
  1. Install the Allure command-line tool (see https://docs.qameta.io/allure/#_installing_a_commandline).
  2. Run the following commands in PowerShell from your project root:
     ```powershell
     allure generate target/allure-results --clean -o target/allure-report
     allure open target/allure-report
     ```
  3. The Allure HTML report will open in your browser.

