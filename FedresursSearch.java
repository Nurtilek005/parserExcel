//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.time.Duration;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class FedresursSearch {
//
//    static String selectedCategory = "";
//    static String fromDate = "";
//    static String toDate = "";
//
//    public static void main(String[] args) {
//        // Запуск интерфейса
//        createGUI();
//    }
//
//    private static void createGUI() {
//        JFrame frame = new JFrame("Парсинг с сайта Bankrotbaza");
//        frame.setSize(400, 250);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel();
//        frame.add(panel);
//        placeComponents(panel);
//
//        frame.setVisible(true);
//    }
//
//    private static void placeComponents(JPanel panel) {
//        panel.setLayout(null);
//
//        // Поле для ввода категории
//        JLabel categoryLabel = new JLabel("Введите категорию:");
//        categoryLabel.setBounds(10, 20, 160, 25);
//        panel.add(categoryLabel);
//
//        JTextField categoryField = new JTextField(20);
//        categoryField.setBounds(180, 20, 165, 25);
//        panel.add(categoryField);
//
//        // Поле для ввода даты "От"
//        JLabel fromDateLabel = new JLabel("Дата публикации от:");
//        fromDateLabel.setBounds(10, 60, 160, 25);
//        panel.add(fromDateLabel);
//
//        JTextField fromDateField = new JTextField(20);
//        fromDateField.setBounds(180, 60, 165, 25);
//        panel.add(fromDateField);
//
//        // Поле для ввода даты "До"
//        JLabel toDateLabel = new JLabel("Дата публикации до:");
//        toDateLabel.setBounds(10, 100, 160, 25);
//        panel.add(toDateLabel);
//
//        JTextField toDateField = new JTextField(20);
//        toDateField.setBounds(180, 100, 165, 25);
//        panel.add(toDateField);
//
//        // Кнопка для запуска парсинга
//        JButton startButton = new JButton("Начать парсинг");
//        startButton.setBounds(10, 140, 150, 25);
//        panel.add(startButton);
//
//        // Обработка нажатия кнопки
//        startButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                selectedCategory = categoryField.getText();
//                fromDate = fromDateField.getText();
//                toDate = toDateField.getText();
//
//                if (!selectedCategory.isEmpty()) {
//                    System.out.println("Запуск парсинга для категории: " + selectedCategory);
//                    startParsingWithCategoryAndSource();
//                } else {
//                    JOptionPane.showMessageDialog(null, "Введите категорию!");
//                }
//            }
//        });
//    }
//
//    // Основная логика парсинга с использованием Selenium
//    private static void startParsingWithCategoryAndSource() {
//        WebDriverManager.chromedriver().setup();
//
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//
//        WebDriver driver = new ChromeDriver(options);
//        driver.manage().window().maximize();  // Открываем браузер в полноэкранном режиме
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Данные");
//
//        int rowIndex = 0;
//        Row headerRow = sheet.createRow(rowIndex++);
//        headerRow.createCell(0).setCellValue("Заголовок");
//        headerRow.createCell(1).setCellValue("Начальная цена");
//        headerRow.createCell(2).setCellValue("Шаг повышения");
//        headerRow.createCell(3).setCellValue("Задаток");
//        headerRow.createCell(4).setCellValue("Приём заявок с");
//        headerRow.createCell(5).setCellValue("Приём заявок до");
//        headerRow.createCell(6).setCellValue("Дата проведения");
//        headerRow.createCell(7).setCellValue("Дата публикации");
//
//        Set<String> visitedLinks = new HashSet<>();
//
//        try {
//            driver.get("https://bankrotbaza.ru/search");
//
//            // Выбор категории
//            WebElement categoryButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-id='modal-category-selection']")));
//            categoryButton.click();
//
//            // Вводим категорию
//            WebElement categoryInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Введите название категории']")));
//            categoryInput.sendKeys(selectedCategory);
//
//            // Ждём, пока элемент появится и кликаем по нему
//            WebElement categoryResult = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='category-selection-12']")));
//            categoryResult.click();
//
//            // Нажимаем кнопку "Выбрать"
//            WebElement selectButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Выбрать']")));
//            selectButton.click();
//
//            // Ожидание перед следующим шагом
//            Thread.sleep(1000);
//
//            // Пытаемся кликнуть на элемент "Все источники"
//            WebElement sourceDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='search-filter']/div/div[1]/div/div[1]/div[1]/div/div[5]/div[2]/div[2]")));
//            sourceDropdown.click();
//
//            // Ожидание и выбор "Банкротства" из выпадающего списка
//            WebElement bankruptcyOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Банкротства']")));
//            bankruptcyOption.click();
//            System.out.println("Источник выбран: Банкротства");
//
//            // Ожидание перед следующим шагом
//            Thread.sleep(1000);
//
//            // Прокрутка к элементу "Поиск по датам"
//            WebElement dateSearchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='search-filter']/div/div[1]/div/div[1]/div[2]/div/div[1]/button")));
//
//            // Прокрутка страницы к элементу "Поиск по датам"
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", dateSearchButton);
//            Thread.sleep(1000); // Небольшая задержка для стабильности
//
//            // Нажатие на "Поиск по датам"
//            dateSearchButton.click();
//
//            // Прокрутка на небольшое расстояние для отображения поля "Дата публикации"
//            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 150);");
//            Thread.sleep(500); // Короткая пауза для стабильности
//
//            // Теперь можно выбирать даты для поля "Дата публикации"
//            WebElement fromDateField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='От']")));
//            fromDateField.click();
//            fromDateField.sendKeys(fromDate);
//
//            // Теперь переходим к полю "До"
//            WebElement toDateField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='До']")));
//            toDateField.click();
//            toDateField.sendKeys(toDate);
//
//            // Нажатие на "Применить фильтр"
//            WebElement applyFilterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Применить фильтр']")));
//            applyFilterButton.click();
//            System.out.println("Фильтр применен");
//
//            // Логика для перехода по страницам и парсинга данных
//            boolean hasNextPage = true;
//
//            while (hasNextPage) {
//                List<WebElement> links = driver.findElements(By.xpath("//a[contains(@href, '/lot/')]"));
//
//                for (WebElement link : links) {
//                    String href = link.getAttribute("href");
//
//                    if (visitedLinks.contains(href)) {
//                        continue;
//                    }
//
//                    visitedLinks.add(href);
//
//                    ((JavascriptExecutor) driver).executeScript("window.open(arguments[0].href);", link);
//
//                    String originalWindow = driver.getWindowHandle();
//                    Set<String> windowHandles = driver.getWindowHandles();
//                    for (String windowHandle : windowHandles) {
//                        if (!windowHandle.equals(originalWindow)) {
//                            driver.switchTo().window(windowHandle);
//                            break;
//                        }
//                    }
//
//                    try {
//                        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1")));
//                    } catch (TimeoutException e) {
//                        System.out.println("Элемент не найден, продолжаем с другим объявлением.");
//                        driver.close();
//                        driver.switchTo().window(originalWindow);
//                        continue;
//                    }
//
//                    JavascriptExecutor js = (JavascriptExecutor) driver;
//                    js.executeScript("window.scrollBy(0, 500);");
//
//                    try {
//                        String title = driver.findElement(By.tagName("h1")).getText();
//                        String startPrice = driver.findElement(By.xpath("//*[contains(text(), 'Начальная')]/following-sibling::*[1]")).getText();
//                        String stepPrice = null;
//                        String deposit = null;
//
//                        try {
//                            stepPrice = driver.findElement(By.xpath("//*[contains(text(), 'Шаг повышения')]/following-sibling::*[1]")).getText();
//                        } catch (NoSuchElementException e) {
//                            System.out.println("Шаг повышения не найден.");
//                        }
//
//                        try {
//                            deposit = driver.findElement(By.xpath("//*[contains(text(), 'Задаток')]/following-sibling::*[1]")).getText();
//                        } catch (NoSuchElementException e) {
//                            System.out.println("Задаток не найден.");
//                        }
//
//                        String startDate = null;
//                        String endDate = null;
//                        String auctionDate = null;
//                        String publicationDate = null;
//
//                        try {
//                            startDate = driver.findElement(By.xpath("//span[text()='Приём заявок с']/following-sibling::span[@class='lot-info__value']")).getText();
//                            endDate = driver.findElement(By.xpath("//span[text()='Приём заявок до']/following-sibling::span[@class='lot-info__value']")).getText();
//                        } catch (NoSuchElementException e) {
//                            System.out.println("Даты подачи заявок не найдены.");
//                        }
//
//                        try {
//                            auctionDate = driver.findElement(By.xpath("//span[text()='Дата проведения']/following-sibling::span[@class='lot-info__value']")).getText();
//                        } catch (NoSuchElementException e) {
//                            System.out.println("Дата проведения торгов не найдена.");
//                        }
//
//                        try {
//                            publicationDate = driver.findElement(By.xpath("//span[text()='Дата публикации']/following-sibling::span[@class='lot-info__value']")).getText();
//                        } catch (NoSuchElementException e) {
//                            System.out.println("Дата публикации не найдена.");
//                        }
//
//                        Row row = sheet.createRow(rowIndex++);
//                        row.createCell(0).setCellValue(title);
//                        row.createCell(1).setCellValue(startPrice != null ? startPrice : "Не найдено");
//                        row.createCell(2).setCellValue(stepPrice != null ? stepPrice : "Не найдено");
//                        row.createCell(3).setCellValue(deposit != null ? deposit : "Не найдено");
//                        row.createCell(4).setCellValue(startDate != null ? startDate : "Не найдено");
//                        row.createCell(5).setCellValue(endDate != null ? endDate : "Не найдено");
//                        row.createCell(6).setCellValue(auctionDate != null ? auctionDate : "Не найдено");
//                        row.createCell(7).setCellValue(publicationDate != null ? publicationDate : "Не найдено");
//
//                        System.out.println("Данные записаны для объявления: " + title);
//                    } catch (NoSuchElementException e) {
//                        System.out.println("Не удалось найти элемент: " + e.getMessage());
//                    }
//
//                    driver.close();
//                    driver.switchTo().window(originalWindow);
//                }
//
//                try {
//                    List<WebElement> nextPageButtons = driver.findElements(By.xpath("//a[contains(text(), 'След. страница')]"));
//
//                    if (nextPageButtons.size() > 0) {
//                        WebElement nextPageButton = nextPageButtons.get(0);
//                        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextPageButton);
//                        Thread.sleep(500);
//
//                        try {
//                            nextPageButton.click();
//                        } catch (ElementClickInterceptedException e) {
//                            System.out.println("Элемент перекрыт, кликаем через JS.");
//                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextPageButton);
//                        }
//
//                        Thread.sleep(3000);
//                    } else {
//                        System.out.println("Кнопка 'След. страница' не найдена, завершаем.");
//                        hasNextPage = false;
//                    }
//                } catch (Exception e) {
//                    System.out.println("Ошибка при обработке кнопки 'След. страница': " + e.getMessage());
//                    hasNextPage = false;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            driver.quit();
//
//            try (FileOutputStream fileOut = new FileOutputStream("C:/Users/user/Documents/parser.xlsx")) {
//                workbook.write(fileOut);
//                System.out.println("Файл успешно сохранен: C:/Users/user/Documents/parser.xlsx");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                workbook.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.time.Duration;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class FedresursSearch {
//
//    static String selectedCategory = "";
//    static String startDate = "";
//    static String endDate = "";
//
//    public static void main(String[] args) {
//        // Запуск интерфейса
//        createGUI();
//    }
//
//    private static void createGUI() {
//        JFrame frame = new JFrame("Парсинг с сайта Bankrotbaza");
//        frame.setSize(500, 300);  // Увеличиваем размер окна
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel();
//        frame.add(panel);
//        placeComponents(panel);
//
//        frame.setVisible(true);
//    }
//
//    private static void placeComponents(JPanel panel) {
//        panel.setLayout(null);
//
//        // Поле для ввода категории
//        JLabel categoryLabel = new JLabel("Введите категорию:");
//        categoryLabel.setBounds(10, 20, 160, 25);
//        panel.add(categoryLabel);
//
//        JTextField categoryField = new JTextField(20);
//        categoryField.setBounds(180, 20, 165, 25);
//        panel.add(categoryField);
//
//        // Поле для выбора даты "От"
//        JLabel startDateLabel = new JLabel("Дата публикации От (dd-MM-yyyy):");
//        startDateLabel.setBounds(10, 60, 200, 25);
//        panel.add(startDateLabel);
//
//        JTextField startDateField = new JTextField(10);
//        startDateField.setBounds(210, 60, 165, 25);
//        panel.add(startDateField);
//
//        // Поле для выбора даты "До"
//        JLabel endDateLabel = new JLabel("Дата публикации До (dd-MM-yyyy):");
//        endDateLabel.setBounds(10, 100, 200, 25);
//        panel.add(endDateLabel);
//
//        JTextField endDateField = new JTextField(10);
//        endDateField.setBounds(210, 100, 165, 25);
//        panel.add(endDateField);
//
//        // Кнопка для запуска парсинга
//        JButton startButton = new JButton("Начать парсинг");
//        startButton.setBounds(10, 150, 150, 25);
//        panel.add(startButton);
//
//        // Обработка нажатия кнопки
//        startButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                selectedCategory = categoryField.getText();
//                startDate = startDateField.getText();
//                endDate = endDateField.getText();
//
//                if (!selectedCategory.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()) {
//                    System.out.println("Запуск парсинга для категории: " + selectedCategory);
//                    startParsingWithCategoryAndDates();
//                } else {
//                    JOptionPane.showMessageDialog(null, "Введите все данные!");
//                }
//            }
//        });
//    }
//
//    // Основная логика парсинга с использованием Selenium
//    private static void startParsingWithCategoryAndDates() {
//        WebDriverManager.chromedriver().setup();
//
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//
//        WebDriver driver = new ChromeDriver(options);
//        driver.manage().window().maximize();  // Открываем браузер в полноэкранном режиме
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Данные");
//
//        int rowIndex = 0;
//        Row headerRow = sheet.createRow(rowIndex++);
//        headerRow.createCell(0).setCellValue("Заголовок");
//        headerRow.createCell(1).setCellValue("Начальная цена");
//        headerRow.createCell(2).setCellValue("Шаг повышения");
//        headerRow.createCell(3).setCellValue("Задаток");
//        headerRow.createCell(4).setCellValue("Приём заявок с");
//        headerRow.createCell(5).setCellValue("Приём заявок до");
//        headerRow.createCell(6).setCellValue("Дата проведения");
//        headerRow.createCell(7).setCellValue("Дата публикации");
//
//        Set<String> visitedLinks = new HashSet<>();
//
//        try {
//            driver.get("https://bankrotbaza.ru/search");
//
//            // Выбор категории
//            WebElement categoryButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@data-id='modal-category-selection']")));
//            categoryButton.click();
//
//            // Вводим категорию
//            WebElement categoryInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Введите название категории']")));
//            categoryInput.sendKeys(selectedCategory);
//
//            // Ждём, пока элемент появится и кликаем по нему
//            WebElement categoryResult = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='category-selection-12']")));
//            categoryResult.click();
//
//            // Нажимаем кнопку "Выбрать"
//            WebElement selectButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Выбрать']")));
//            selectButton.click();
//
//            // Ожидание перед следующим шагом
//            Thread.sleep(500);
//
//            // Пытаемся кликнуть на элемент "Все источники"
//            WebElement sourceDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='search-filter']/div/div[1]/div/div[1]/div[1]/div/div[5]/div[2]/div[2]")));
//            sourceDropdown.click();
//
//            // Ожидание и выбор "Банкротства" из выпадающего списка
//            WebElement bankruptcyOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Банкротства']")));
//            bankruptcyOption.click();
//            System.out.println("Источник выбран: Банкротства");
//
//            // Ожидание перед следующим шагом
//            Thread.sleep(500);
//
//            // Прокрутка и клик по "Поиск по датам"
//            WebElement searchByDate = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='search-filter']/div/div[1]/div/div[1]/div[2]/div/div[1]/button")));
//
//            // Прокручиваем страницу к элементу
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", searchByDate);
//            Thread.sleep(200);  // Уменьшаем время паузы
//
//            searchByDate.click();
//
//            // Прокрутка страницы немного дальше, чтобы сделать видимой дату публикации
//            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200)");  // Уменьшаем прокрутку
//
//            // Нажатие на календарь для ввода "От"
//            WebElement dateFromField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='От']")));
//
//            // Клик через JavaScript, если обычный клик не работает
//            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dateFromField);
//
//            // Логика для выбора даты "От"
//            selectDateFromCalendar(driver, wait, startDate);
//
//            // Нажатие на календарь для ввода "До"
//            WebElement dateToField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='До']")));
//
//            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dateToField);
//
//            // Логика для выбора даты "До"
//            selectDateFromCalendar(driver, wait, endDate);
//
//            // Применение фильтра
//            WebElement applyFilterButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Применить фильтр']")));
//            applyFilterButton.click();
//            System.out.println("Фильтр применен");
//
//        } catch (ElementClickInterceptedException e) {
//            System.out.println("Не удалось кликнуть по элементу. Элемент перекрыт.");
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            driver.quit();
//
//            try (FileOutputStream fileOut = new FileOutputStream("C:/Users/user/Documents/parser.xlsx")) {
//                workbook.write(fileOut);
//                System.out.println("Файл успешно сохранен: C:/Users/user/Documents/parser.xlsx");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                workbook.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Логика для выбора даты из календаря
//    private static void selectDateFromCalendar(WebDriver driver, WebDriverWait wait, String date) {
//        String[] dateParts = date.split("-");
//        int targetDay = Integer.parseInt(dateParts[0]);
//        int targetMonth = Integer.parseInt(dateParts[1]);
//        int targetYear = Integer.parseInt(dateParts[2]);
//
//        WebElement currentMonth = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='month-class']"))); // Измените на правильный xpath
//        WebElement currentYear = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='year-class']")));  // Измените на правильный xpath
//
//        // Логика смены года
//        while (Integer.parseInt(currentYear.getText()) != targetYear) {
//            if (Integer.parseInt(currentYear.getText()) > targetYear) {
//                WebElement prevYearButton = driver.findElement(By.xpath("//button[@class='prev-year-button']")); // Измените xpath
//                prevYearButton.click();
//            } else {
//                WebElement nextYearButton = driver.findElement(By.xpath("//button[@class='next-year-button']")); // Измените xpath
//                nextYearButton.click();
//            }
//            currentYear = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='year-class']")));  // Обновляем текущий год
//        }
//
//        // Логика смены месяца
//        while (Integer.parseInt(currentMonth.getText()) != targetMonth) {
//            if (Integer.parseInt(currentMonth.getText()) > targetMonth) {
//                WebElement prevMonthButton = driver.findElement(By.xpath("//button[@class='prev-month-button']")); // Измените xpath
//                prevMonthButton.click();
//            } else {
//                WebElement nextMonthButton = driver.findElement(By.xpath("//button[@class='next-month-button']")); // Измените xpath
//                nextMonthButton.click();
//            }
//            currentMonth = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='month-class']")));  // Обновляем текущий месяц
//        }
//
//        // Теперь выбираем день
//        WebElement dayButton = driver.findElement(By.xpath("//button[text()='" + targetDay + "']")); // Найти кнопку с нужным днем
//        dayButton.click();
//    }
//}












                                      //  парсер Гевея
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Iterator;
//
//public class FedresursSearch {
//
//    public static void main(String[] args) {
//        // Настройка окна приложения
//        JFrame frame = new JFrame("Категории и Реестры");
//        frame.setSize(400, 500);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // Создаем панель с вертикальной компоновкой
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
//        // Кнопка для выбора файла Excel с фамилиями
//        JButton chooseFileButton = new JButton("Выбрать файл с фамилиями");
//        JLabel filePathLabel = new JLabel("Файл с фамилиями не выбран");
//
//        // Кнопка для выбора файла Excel для сохранения результатов
//        JButton chooseSaveFileButton = new JButton("Выбрать файл для сохранения");
//        JLabel saveFilePathLabel = new JLabel("Файл для сохранения не выбран");
//
//        // Кнопка для реестра Арбитражных управляющих
//        JButton arbitratorsButton = new JButton("Реестр Арбитражных управляющих");
//
//        // Событие выбора файла с фамилиями
//        chooseFileButton.addActionListener(e -> {
//            JFileChooser fileChooser = new JFileChooser();
//            int returnValue = fileChooser.showOpenDialog(null);
//            if (returnValue == JFileChooser.APPROVE_OPTION) {
//                File selectedFile = fileChooser.getSelectedFile();
//                filePathLabel.setText(selectedFile.getAbsolutePath());  // Показать путь к файлу
//            }
//        });
//
//        // Событие выбора файла для сохранения
//        chooseSaveFileButton.addActionListener(e -> {
//            JFileChooser fileChooser = new JFileChooser();
//            int returnValue = fileChooser.showSaveDialog(null);
//            if (returnValue == JFileChooser.APPROVE_OPTION) {
//                File saveFile = fileChooser.getSelectedFile();
//                saveFilePathLabel.setText(saveFile.getAbsolutePath());  // Показать путь для сохранения
//            }
//        });
//
//        // Событие для кнопки "Реестр Арбитражных управляющих"
//        arbitratorsButton.addActionListener(e -> {
//            String excelFilePath = filePathLabel.getText();
//            String saveFilePath = saveFilePathLabel.getText();
//            if (!excelFilePath.equals("Файл с фамилиями не выбран") && !saveFilePath.equals("Файл для сохранения не выбран")) {
//                openRegistryPageAndSearch("https://heveya.ru/registry/arbitrators", excelFilePath, saveFilePath);
//            } else {
//                JOptionPane.showMessageDialog(frame, "Пожалуйста, выберите файлы Excel для фамилий и сохранения результатов", "Ошибка", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//
//        // Добавляем элементы на панель
//        panel.add(chooseFileButton);
//        panel.add(filePathLabel);
//        panel.add(chooseSaveFileButton);
//        panel.add(saveFilePathLabel);
//        panel.add(arbitratorsButton);
//
//        // Добавляем панель в окно
//        frame.add(panel);
//        frame.setVisible(true);
//    }
//
//    // Метод для открытия ссылки в браузере, выполнения поиска через Selenium и записи результатов в новый Excel
//    private static void openRegistryPageAndSearch(String url, String excelFilePath, String saveFilePath) {
//        // Настройка Selenium WebDriver для Chrome
//        WebDriverManager.chromedriver().setup();
//        ChromeOptions options = new ChromeOptions();
//        WebDriver driver = new ChromeDriver(options);
//
//        try {
//            // Открываем браузер на полный экран
//            driver.manage().window().maximize();
//
//            // Открываем страницу реестра
//            driver.get(url);
//            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//
//            // Читаем данные из Excel
//            FileInputStream fis = new FileInputStream(excelFilePath);
//            Workbook workbook = new XSSFWorkbook(fis);
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//
//            // Создаем новый Excel файл для записи результатов
//            Workbook resultWorkbook = new XSSFWorkbook();
//            Sheet resultSheet = resultWorkbook.createSheet("Результаты");
//
//            int rowIndex = 0;
//
//            // Проходим по каждой строке Excel и ищем фамилии
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//
//                // Проверяем, существует ли ячейка и не пуста ли она
//                if (row.getCell(0) == null) {
//                    System.out.println("Пустая ячейка, пропуск строки.");
//                    continue;
//                }
//
//                String lastName = row.getCell(0).getStringCellValue(); // Берем фамилию из первой колонки
//                System.out.println("Поиск по фамилии: " + lastName);
//
//                // Вводим фамилию в поле поиска на странице
//                WebElement searchBox = driver.findElement(By.xpath("//input[@placeholder='Поиск по реестру']"));
//                searchBox.clear();
//                searchBox.sendKeys(lastName);
//
//                // Ждем, пока кнопка лупы станет доступной, и нажимаем на нее
//                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//                WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='baseButton baseButton_type_iconOnly baseButton_theme_black baseButton_size_medium']")));
//                searchButton.click();
//
//                // Ждем, чтобы результаты загрузились
//                Thread.sleep(5000);
//
//                // Парсим необходимые данные с экрана
//                String foundName = getElementText(driver, "//div[contains(@class,'name')]", "null");
//                String phone = getElementText(driver, "//div[contains(@class,'phone')]/a", "null");
//                String email = getElementText(driver, "//a[contains(@href, 'mailto:')]", "null");
//                String regNumber = getElementText(driver, "//span[contains(text(), 'Регистрационный номер:')]/following-sibling::span", "null");
//                String regDate = getElementText(driver, "//span[contains(text(), 'Дата регистрации:')]/following-sibling::span", "null");
//                String inn = getElementText(driver, "//span[contains(text(), 'ИНН:')]/following-sibling::span", "null");
//                String city = getElementText(driver, "//a[contains(@class, 'region')]", "null");  // Город ниже имени
//                String totalTrades = getElementText(driver, "//span[contains(text(), 'Объявлено торгов:')]/following-sibling::span", "null");
//                String failedTrades = getElementText(driver, "//span[contains(text(), 'Не состоялось торгов:')]/following-sibling::span", "null");
//                String completedTrades = getElementText(driver, "//span[contains(text(), 'Завершено торгов:')]/following-sibling::span", "null");
//                String activeTrades = getElementText(driver, "//span[contains(text(), 'Активные торги:')]/following-sibling::span", "null");
//                String sro = getElementText(driver, "//span[contains(text(), 'СРО:')]/following-sibling::span", "null");
//
//                System.out.println("ФИО: " + foundName);
//                System.out.println("Телефон: " + phone);
//                System.out.println("Email: " + email);
//                System.out.println("Регистрационный номер: " + regNumber);
//                System.out.println("Дата регистрации: " + regDate);
//                System.out.println("ИНН: " + inn);
//                System.out.println("Город: " + city);
//                System.out.println("Объявлено торгов: " + totalTrades);
//                System.out.println("Не состоялось торгов: " + failedTrades);
//                System.out.println("Завершено торгов: " + completedTrades);
//                System.out.println("Активные торги: " + activeTrades);
//                System.out.println("СРО: " + sro);
//
//                // Записываем данные в новый Excel файл
//                Row resultRow = resultSheet.createRow(rowIndex++);
//                resultRow.createCell(0).setCellValue(lastName);
//                resultRow.createCell(1).setCellValue(foundName);
//                resultRow.createCell(2).setCellValue(phone);
//                resultRow.createCell(3).setCellValue(email);
//                resultRow.createCell(4).setCellValue(regNumber);
//                resultRow.createCell(5).setCellValue(regDate);
//                resultRow.createCell(6).setCellValue(inn);
//                resultRow.createCell(7).setCellValue(city);
//                resultRow.createCell(8).setCellValue(totalTrades);
//                resultRow.createCell(9).setCellValue(failedTrades);
//                resultRow.createCell(10).setCellValue(completedTrades);
//                resultRow.createCell(11).setCellValue(activeTrades);
//                resultRow.createCell(12).setCellValue(sro);
//            }
//
//            // Закрываем исходный Excel файл
//            workbook.close();
//            fis.close();
//
//            // Сохраняем новый Excel файл с результатами
//            FileOutputStream fos = new FileOutputStream(saveFilePath);
//            resultWorkbook.write(fos);
//            resultWorkbook.close();
//            fos.close();
//
//            JOptionPane.showMessageDialog(null, "Результаты успешно сохранены!");
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            // Закрываем браузер после завершения
//            driver.quit();
//        }
//    }
//
//    // Метод для безопасного извлечения текста элемента
//    private static String getElementText(WebDriver driver, String xpath, String defaultValue) {
//        try {
//            WebElement element = driver.findElement(By.xpath(xpath));
//            return element.getText();
//        } catch (Exception e) {
//            return defaultValue;
//        }
//    }
//}





















//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class FedresursSearch {
//    public static void main(String[] args) throws IOException {
//        // Путь к исходному файлу Excel
//        String excelFilePath = "C:\\Users\\user\\Documents\\Книга1.xlsx"; // Укажите ваш файл
//        String newExcelFilePath = "C:\\Users\\user\\Documents\\Новая екселька.xlsx";  // Новый файл для сохранения разделенных данных
//
//        // Чтение исходного Excel файла
//        FileInputStream fis = new FileInputStream(new File(excelFilePath));
//        Workbook workbook = new XSSFWorkbook(fis);
//        Sheet sheet = workbook.getSheetAt(0);  // Чтение первого листа
//
//        // Создаем новый Excel файл для записи разделенных данных
//        Workbook newWorkbook = new XSSFWorkbook();
//        Sheet newSheet = newWorkbook.createSheet("Разделенные данные");
//
//        int newRowIdxN = 0; // Индекс для новой строки в колонке N
//        int newRowIdxP = 0; // Индекс для новой строки в колонке P
//        int newRowIdxU = 0; // Индекс для новой строки в колонке U
//        int newRowIdxV = 0; // Индекс для новой строки в колонке V
//
//        // Проход по строкам и обработка колонок N (индекс 13), P (индекс 15), U (индекс 20), V (индекс 21)
//        for (Row row : sheet) {
//            // Обработка данных из колонки N (индекс 13)
//            Cell cellN = row.getCell(13);
//            if (cellN != null && cellN.getCellType() == CellType.STRING) {
//                String cellValueN = cellN.getStringCellValue();
//                cellValueN = cellValueN.replace("[", "").replace("]", "").trim();
//                String[] splitDataN = cellValueN.split(","); // Разделение по запятой
//
//                // Записываем данные из колонки N в новую колонку N в новом файле
//                for (String data : splitDataN) {
//                    if (!data.trim().isEmpty()) { // Проверка на пустое значение
//                        Row newRow = newSheet.getRow(newRowIdxN);
//                        if (newRow == null) {
//                            newRow = newSheet.createRow(newRowIdxN);
//                        }
//                        Cell newCellN = newRow.createCell(13); // Записываем в колонку N (индекс 13)
//                        newCellN.setCellValue(data.trim());
//                        newRowIdxN++;
//                    }
//                }
//            }
//
//            // Обработка данных из колонки P (индекс 15)
//            Cell cellP = row.getCell(15);
//            if (cellP != null && cellP.getCellType() == CellType.STRING) {
//                String cellValueP = cellP.getStringCellValue();
//                cellValueP = cellValueP.replace("[", "").replace("]", "").trim();
//                String[] splitDataP = cellValueP.split("', '"); // Разделение по ", '"
//
//                // Записываем данные из колонки P в новую колонку P в новом файле
//                for (String data : splitDataP) {
//                    if (!data.trim().isEmpty()) { // Проверка на пустое значение
//                        Row newRow = newSheet.getRow(newRowIdxP);
//                        if (newRow == null) {
//                            newRow = newSheet.createRow(newRowIdxP);
//                        }
//                        Cell newCellP = newRow.createCell(15); // Записываем в колонку P (индекс 15)
//                        newCellP.setCellValue(data.trim());
//                        newRowIdxP++;
//                    }
//                }
//            }
//
//            // Обработка данных из колонки U (индекс 20)
//            Cell cellU = row.getCell(20);
//            if (cellU != null && cellU.getCellType() == CellType.STRING) {
//                String cellValueU = cellU.getStringCellValue();
//                cellValueU = cellValueU.replace("[", "").replace("]", "").trim();
//                String[] splitDataU = cellValueU.split("', '"); // Разделение по ", '"
//
//                // Записываем данные из колонки U в новую колонку U в новом файле
//                for (String data : splitDataU) {
//                    if (!data.trim().isEmpty()) { // Проверка на пустое значение
//                        Row newRow = newSheet.getRow(newRowIdxU);
//                        if (newRow == null) {
//                            newRow = newSheet.createRow(newRowIdxU);
//                        }
//                        Cell newCellU = newRow.createCell(20); // Записываем в колонку U (индекс 20)
//                        newCellU.setCellValue(data.trim());
//                        newRowIdxU++;
//                    }
//                }
//            }
//
//            // Обработка данных из колонки V (индекс 21)
//            Cell cellV = row.getCell(21);
//            if (cellV != null && cellV.getCellType() == CellType.STRING) {
//                String cellValueV = cellV.getStringCellValue();
//                cellValueV = cellValueV.replace("[", "").replace("]", "").trim();
//                String[] splitDataV = cellValueV.split("', '"); // Разделение по ", '"
//
//                // Записываем данные из колонки V в новую колонку V в новом файле
//                for (String data : splitDataV) {
//                    if (!data.trim().isEmpty()) { // Проверка на пустое значение
//                        Row newRow = newSheet.getRow(newRowIdxV);
//                        if (newRow == null) {
//                            newRow = newSheet.createRow(newRowIdxV);
//                        }
//                        Cell newCellV = newRow.createCell(21); // Записываем в колонку V (индекс 21)
//                        newCellV.setCellValue(data.trim());
//                        newRowIdxV++;
//                    }
//                }
//            }
//        }
//
//        // Закрытие исходного Excel файла
//        fis.close();
//
//        // Сохранение нового Excel файла
//        FileOutputStream fos = new FileOutputStream(newExcelFilePath);
//        newWorkbook.write(fos);
//        fos.close();
//        newWorkbook.close();
//
//        System.out.println("Разделенные данные из колонок N, P, U, V успешно сохранены в новый Excel файл.");
//    }
//}
////
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class FedresursSearch {
//    public static void main(String[] args) throws IOException {
//        // Путь к исходному файлу Excel
//        String excelFilePath = "C:\\Users\\user\\Documents\\Книга1.xlsx"; // Укажите ваш файл
//        String newExcelFilePath = "C:\\Users\\user\\Documents\\Новая екселька.xlsx";  // Новый файл для сохранения разделенных данных
//
//        // Чтение исходного Excel файла
//        FileInputStream fis = new FileInputStream(new File(excelFilePath));
//        Workbook workbook = new XSSFWorkbook(fis);
//        Sheet sheet = workbook.getSheetAt(0);  // Чтение первого листа
//
//        // Создаем новый Excel файл для записи разделенных данных
//        Workbook newWorkbook = new XSSFWorkbook();
//        Sheet newSheet = newWorkbook.createSheet("Разделенные данные");
//
//        int newRowIdx = 0; // Индекс для новой строки в новом файле
//
//        // Проход по строкам и обработка колонок N (индекс 13), P (индекс 15), U (индекс 20), V (индекс 21)
//        for (Row row : sheet) {
//            // Получаем разделенные данные для колонки N (по запятой)
//            String[] splitDataN = getSplitDataByComma(row, 13); // Колонка N
//
//            // Получаем разделенные данные для колонок P, U, V (по ", ")
//            String[] splitDataP = getSplitDataByCommaAndSpace(row, 15); // Колонка P
//            String[] splitDataU = getSplitDataByCommaAndSpace(row, 20); // Колонка U
//            String[] splitDataV = getSplitDataByCommaAndSpace(row, 21); // Колонка V
//
//            // Определяем максимальное количество разделенных данных (учитываются N, P, U, V)
//            int maxSplits = Math.max(splitDataN.length, Math.max(splitDataP.length, Math.max(splitDataU.length, splitDataV.length)));
//
//            // Для каждой строки разделения копируем остальные данные
//            for (int i = 0; i < maxSplits; i++) {
//                Row newRow = newSheet.createRow(newRowIdx++);
//
//                // Копируем все данные из исходной строки, кроме N, P, U, V
//                copyRow(row, newRow);
//
//                // Записываем разделенные данные в соответствующие колонки
//                if (i < splitDataN.length) {
//                    newRow.createCell(13).setCellValue(splitDataN[i]);
//                }
//                if (i < splitDataP.length) {
//                    newRow.createCell(15).setCellValue(splitDataP[i]);
//                }
//                if (i < splitDataU.length) {
//                    newRow.createCell(20).setCellValue(splitDataU[i]);
//                }
//                if (i < splitDataV.length) {
//                    newRow.createCell(21).setCellValue(splitDataV[i]);
//                }
//            }
//        }
//
//        // Закрытие исходного Excel файла
//        fis.close();
//
//        // Сохранение нового Excel файла
//        FileOutputStream fos = new FileOutputStream(newExcelFilePath);
//        newWorkbook.write(fos);
//        fos.close();
//        newWorkbook.close();
//
//        System.out.println("Разделенные данные успешно сохранены в новый Excel файл.");
//    }
//
//    // Метод для получения разделенных данных по запятой (для колонки N)
//    private static String[] getSplitDataByComma(Row row, int colIndex) {
//        Cell cell = row.getCell(colIndex);
//        if (cell != null && cell.getCellType() == CellType.STRING) {
//            String cellValue = cell.getStringCellValue();
//            return cellValue.replace("[", "").replace("]", "").split(","); // Разделение по запятой
//        }
//        return new String[]{""}; // Если данных нет, возвращаем пустое значение
//    }
//
//    // Метод для получения разделенных данных по ", " (для колонок P, U, V)
//    private static String[] getSplitDataByCommaAndSpace(Row row, int colIndex) {
//        Cell cell = row.getCell(colIndex);
//        if (cell != null && cell.getCellType() == CellType.STRING) {
//            String cellValue = cell.getStringCellValue();
//            return cellValue.replace("[", "").replace("]", "").split("', '"); // Разделение по ", "
//        }
//        return new String[]{""}; // Если данных нет, возвращаем пустое значение
//    }
//
//    // Метод для копирования строки, исключая колонки N, P, U, V
//    private static void copyRow(Row sourceRow, Row targetRow) {
//        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
//            if (i != 13 && i != 15 && i != 20 && i != 21) { // Пропускаем колонки N, P, U, V
//                Cell sourceCell = sourceRow.getCell(i);
//                if (sourceCell != null) {
//                    Cell targetCell = targetRow.createCell(i);
//                    copyCell(sourceCell, targetCell);
//                }
//            }
//        }
//    }
//
//    // Метод для копирования данных ячейки
//    private static void copyCell(Cell sourceCell, Cell targetCell) {
//        switch (sourceCell.getCellType()) {
//            case STRING:
//                targetCell.setCellValue(sourceCell.getStringCellValue());
//                break;
//            case NUMERIC:
//                targetCell.setCellValue(sourceCell.getNumericCellValue());
//                break;
//            case BOOLEAN:
//                targetCell.setCellValue(sourceCell.getBooleanCellValue());
//                break;
//            case FORMULA:
//                targetCell.setCellFormula(sourceCell.getCellFormula());
//                break;
//            default:
//                break;
//        }
//    }
//}

//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class FedresursSearch {
//
//    public static void main(String[] args) {
//        // Создаем окно для интерфейса
//        JFrame frame = new JFrame("Разделение данных в Excel");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(600, 200);
//
//        // Создаем панель для размещения компонентов
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(3, 3));
//
//        // Метки и текстовые поля для выбора файлов
//        JLabel labelInput = new JLabel("Исходный файл Excel:");
//        JTextField inputFileField = new JTextField();
//        JButton inputFileButton = new JButton("Выбрать...");
//
//        JLabel labelOutput = new JLabel("Сохраняемый файл Excel:");
//        JTextField outputFileField = new JTextField();
//        JButton outputFileButton = new JButton("Выбрать...");
//
//        // Кнопка для запуска процесса
//        JButton processButton = new JButton("Запустить");
//
//        // Добавляем элементы на панель
//        panel.add(labelInput);
//        panel.add(inputFileField);
//        panel.add(inputFileButton);
//
//        panel.add(labelOutput);
//        panel.add(outputFileField);
//        panel.add(outputFileButton);
//
//        panel.add(new JLabel()); // Пустое место для выравнивания
//        panel.add(processButton);
//
//        // Добавляем панель на окно
//        frame.add(panel);
//        frame.setVisible(true);
//
//        // Обработчик выбора исходного файла
//        inputFileButton.addActionListener(e -> {
//            String selectedFile = chooseFile("Выберите исходный файл Excel");
//            if (selectedFile != null) {
//                inputFileField.setText(selectedFile);
//            }
//        });
//
//        // Обработчик выбора файла для сохранения
//        outputFileButton.addActionListener(e -> {
//            String selectedFile = chooseFileForSave("Выберите файл для сохранения");
//            if (selectedFile != null) {
//                outputFileField.setText(selectedFile);
//            }
//        });
//
//        // Обработчик нажатия на кнопку "Запустить"
//        processButton.addActionListener(e -> {
//            String inputFilePath = inputFileField.getText();
//            String outputFilePath = outputFileField.getText();
//            if (!inputFilePath.isEmpty() && !outputFilePath.isEmpty()) {
//                try {
//                    processExcel(inputFilePath, outputFilePath);
//                    JOptionPane.showMessageDialog(frame, "Процесс завершен! Данные сохранены.");
//                } catch (IOException ex) {
//                    JOptionPane.showMessageDialog(frame, "Ошибка при обработке файлов: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(frame, "Пожалуйста, выберите оба файла!", "Ошибка", JOptionPane.WARNING_MESSAGE);
//            }
//        });
//    }
//
//    // Метод для обработки Excel файла
//    private static void processExcel(String excelFilePath, String newExcelFilePath) throws IOException {
//        // Чтение исходного Excel файла
//        FileInputStream fis = new FileInputStream(new File(excelFilePath));
//        Workbook workbook = new XSSFWorkbook(fis);
//        Sheet sheet = workbook.getSheetAt(0);  // Чтение первого листа
//
//        // Создаем новый Excel файл для записи разделенных данных
//        Workbook newWorkbook = new XSSFWorkbook();
//        Sheet newSheet = newWorkbook.createSheet("Разделенные данные");
//
//        int newRowIdx = 0; // Индекс для новой строки в новом файле
//
//        // Проход по строкам и обработка колонок N (индекс 13), P (индекс 15), U (индекс 20), V (индекс 21)
//        for (Row row : sheet) {
//            // Получаем разделенные данные для колонки N (по запятой)
//            String[] splitDataN = getSplitDataByComma(row, 26); // Колонка N
//
//            // Получаем разделенные данные для колонок P, U, V (по ", ")
//            String[] splitDataP = getSplitDataByCommaAndSpace(row, 27); // Колонка P
//            String[] splitDataU = getSplitDataByCommaAndSpace(row, 30); // Колонка U
//            String[] splitDataV = getSplitDataByCommaAndSpace(row, 29); // Колонка V
//            String[] splitDataAD = getSplitDataByCommaAndSpace(row, 28);
//
//            // Определяем максимальное количество разделенных данных (учитываются N, P, U, V)
//            int maxSplits = Math.max(splitDataN.length, Math.max(splitDataP.length, Math.max(splitDataU.length, splitDataV.length)));
//
//            // Для каждой строки разделения копируем остальные данные
//            for (int i = 0; i < maxSplits; i++) {
//                Row newRow = newSheet.createRow(newRowIdx++);
//
//                // Копируем все данные из исходной строки, кроме N, P, U, V
//                copyRow(row, newRow);
//
//                // Записываем разделенные данные в соответствующие колонки
//                if (i < splitDataN.length) {
//                    newRow.createCell(26).setCellValue(splitDataN[i]);
//                }
//                if (i < splitDataP.length) {
//                    newRow.createCell(27).setCellValue(splitDataP[i]);
//                }
//                if (i < splitDataU.length) {
//                    newRow.createCell(30).setCellValue(splitDataU[i]);
//                }
//                if (i < splitDataV.length) {
//                    newRow.createCell(29).setCellValue(splitDataV[i]);
//                }
//            }
//        }
//
//        // Закрытие исходного Excel файла
//        fis.close();
//
//        // Сохранение нового Excel файла
//        FileOutputStream fos = new FileOutputStream(newExcelFilePath);
//        newWorkbook.write(fos);
//        fos.close();
//        newWorkbook.close();
//    }
//
//    // Метод для получения разделенных данных по запятой (для колонки N)
//    private static String[] getSplitDataByComma(Row row, int colIndex) {
//        Cell cell = row.getCell(colIndex);
//        if (cell != null && cell.getCellType() == CellType.STRING) {
//            String cellValue = cell.getStringCellValue();
//            return cellValue.replace("[", "").replace("]", "").split(","); // Разделение по запятой
//        }
//        return new String[]{""}; // Если данных нет, возвращаем пустое значение
//    }
//
//    // Метод для получения разделенных данных по ", " (для колонок P, U, V)
//    private static String[] getSplitDataByCommaAndSpace(Row row, int colIndex) {
//        Cell cell = row.getCell(colIndex);
//        if (cell != null && cell.getCellType() == CellType.STRING) {
//            String cellValue = cell.getStringCellValue();
//            return cellValue.replace("[", "").replace("]", "").split("', '"); // Разделение по ", "
//        }
//        return new String[]{""}; // Если данных нет, возвращаем пустое значение
//    }
//
//    // Метод для копирования строки, исключая колонки N, P, U, V
//    private static void copyRow(Row sourceRow, Row targetRow) {
//        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
//            if (i != 26 && i != 27 && i != 30 && i != 29) { // Пропускаем колонки N, P, U, V
//                Cell sourceCell = sourceRow.getCell(i);
//                if (sourceCell != null) {
//                    Cell targetCell = targetRow.createCell(i);
//                    copyCell(sourceCell, targetCell);
//                }
//            }
//        }
//    }
//
//    // Метод для копирования данных ячейки
//    private static void copyCell(Cell sourceCell, Cell targetCell) {
//        switch (sourceCell.getCellType()) {
//            case STRING:
//                targetCell.setCellValue(sourceCell.getStringCellValue());
//                break;
//            case NUMERIC:
//                targetCell.setCellValue(sourceCell.getNumericCellValue());
//                break;
//            case BOOLEAN:
//                targetCell.setCellValue(sourceCell.getBooleanCellValue());
//                break;
//            case FORMULA:
//                targetCell.setCellFormula(sourceCell.getCellFormula());
//                break;
//            default:
//                break;
//        }
//    }
//
//    // Метод для выбора файла с помощью JFileChooser
//    private static String chooseFile(String dialogTitle) {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle(dialogTitle);
//        int result = fileChooser.showOpenDialog(null);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            return fileChooser.getSelectedFile().getAbsolutePath();
//        }
//        return null;
//    }
//
//    // Метод для выбора места сохранения файла
//    private static String chooseFileForSave(String dialogTitle) {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle(dialogTitle);
//        int result = fileChooser.showSaveDialog(null);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            return fileChooser.getSelectedFile().getAbsolutePath();
//        }
//        return null;
//    }
//}





//                                                                рабочий разделитель
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class FedresursSearch {
//
//    public static void main(String[] args) {
//        // Создаем окно для интерфейса
//        JFrame frame = new JFrame("Разделение данных в Excel");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(600, 200);
//
//        // Создаем панель для размещения компонентов
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(3, 3));
//
//        // Метки и текстовые поля для выбора файлов
//        JLabel labelInput = new JLabel("Исходный файл Excel:");
//        JTextField inputFileField = new JTextField();
//        JButton inputFileButton = new JButton("Выбрать...");
//
//        JLabel labelOutput = new JLabel("Сохраняемый файл Excel:");
//        JTextField outputFileField = new JTextField();
//        JButton outputFileButton = new JButton("Выбрать...");
//
//        // Кнопка для запуска процесса
//        JButton processButton = new JButton("Запустить");
//
//        // Добавляем элементы на панель
//        panel.add(labelInput);
//        panel.add(inputFileField);
//        panel.add(inputFileButton);
//
//        panel.add(labelOutput);
//        panel.add(outputFileField);
//        panel.add(outputFileButton);
//
//        panel.add(new JLabel()); // Пустое место для выравнивания
//        panel.add(processButton);
//
//        // Добавляем панель на окно
//        frame.add(panel);
//        frame.setVisible(true);
//
//        // Обработчик выбора исходного файла
//        inputFileButton.addActionListener(e -> {
//            String selectedFile = chooseFile("Выберите исходный файл Excel");
//            if (selectedFile != null) {
//                inputFileField.setText(selectedFile);
//            }
//        });
//
//        // Обработчик выбора файла для сохранения
//        outputFileButton.addActionListener(e -> {
//            String selectedFile = chooseFileForSave("Выберите файл для сохранения");
//            if (selectedFile != null) {
//                outputFileField.setText(selectedFile);
//            }
//        });
//
//        // Обработчик нажатия на кнопку "Запустить"
//        processButton.addActionListener(e -> {
//            String inputFilePath = inputFileField.getText();
//            String outputFilePath = outputFileField.getText();
//            if (!inputFilePath.isEmpty() && !outputFilePath.isEmpty()) {
//                try {
//                    processExcel(inputFilePath, outputFilePath);
//                    JOptionPane.showMessageDialog(frame, "Процесс завершен! Данные сохранены.");
//                } catch (IOException ex) {
//                    JOptionPane.showMessageDialog(frame, "Ошибка при обработке файлов: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(frame, "Пожалуйста, выберите оба файла!", "Ошибка", JOptionPane.WARNING_MESSAGE);
//            }
//        });
//    }
//
//    // Метод для обработки Excel файла
//    private static void processExcel(String excelFilePath, String newExcelFilePath) throws IOException {
//        // Чтение исходного Excel файла
//        FileInputStream fis = new FileInputStream(new File(excelFilePath));
//        Workbook workbook = new XSSFWorkbook(fis);
//        Sheet sheet = workbook.getSheetAt(0);  // Чтение первого листа
//
//        // Создаем новый Excel файл для записи разделенных данных
//        Workbook newWorkbook = new XSSFWorkbook();
//        Sheet newSheet = newWorkbook.createSheet("Разделенные данные");
//
//        int newRowIdx = 0; // Индекс для новой строки в новом файле
//
//        // Проход по строкам и обработка колонок N (индекс 13), P (индекс 15), U (индекс 20), V (индекс 21), W (новая колонка)
//        for (Row row : sheet) {
//            // Получаем разделенные данные для колонки N (по запятой)
//            String[] splitDataN = getSplitDataByComma(row, 26); // Колонка N
//
//            // Получаем разделенные данные для колонок P, U, V (по ", ")
//            String[] splitDataP = getSplitDataByCommaAndSpace(row, 27); // Колонка P
//            String[] splitDataU = getSplitDataByCommaAndSpace(row, 28); // Колонка U
//            String[] splitDataV = getSplitDataByCommaAndSpace(row, 29); // Колонка V
//            String[] splitDataW = getSplitDataByCommaAndSpace(row, 30); // Новая колонка W
//
//            // Определяем максимальное количество разделенных данных (учитываются N, P, U, V, W)
//            int maxSplits = Math.max(splitDataN.length, Math.max(splitDataP.length, Math.max(splitDataU.length, Math.max(splitDataV.length, splitDataW.length))));
//
//            // Для каждой строки разделения копируем остальные данные
//            for (int i = 0; i < maxSplits; i++) {
//                Row newRow = newSheet.createRow(newRowIdx++);
//
//                // Копируем все данные из исходной строки, кроме N, P, U, V, W
//                copyRow(row, newRow);
//
//                // Записываем разделенные данные в соответствующие колонки
//                if (i < splitDataN.length) {
//                    newRow.createCell(26).setCellValue(splitDataN[i]);
//                }
//                if (i < splitDataP.length) {
//                    newRow.createCell(27).setCellValue(splitDataP[i]);
//                }
//                if (i < splitDataU.length) {
//                    newRow.createCell(28).setCellValue(splitDataU[i]);
//                }
//                if (i < splitDataV.length) {
//                    newRow.createCell(29).setCellValue(splitDataV[i]);
//                }
//                if (i < splitDataW.length) {
//                    newRow.createCell(30).setCellValue(splitDataW[i]);
//                }
//            }
//        }
//
//        // Закрытие исходного Excel файла
//        fis.close();
//
//        // Сохранение нового Excel файла
//        FileOutputStream fos = new FileOutputStream(newExcelFilePath);
//        newWorkbook.write(fos);
//        fos.close();
//        newWorkbook.close();
//    }
//
//    // Метод для получения разделенных данных по запятой (для колонки N)
//    private static String[] getSplitDataByComma(Row row, int colIndex) {
//        Cell cell = row.getCell(colIndex);
//        if (cell != null && cell.getCellType() == CellType.STRING) {
//            String cellValue = cell.getStringCellValue();
//            return cellValue.replace("[", "").replace("]", "").split(","); // Разделение по запятой
//        }
//        return new String[]{""}; // Если данных нет, возвращаем пустое значение
//    }
//
//    // Метод для получения разделенных данных по ", " (для колонок P, U, V, W)
//    private static String[] getSplitDataByCommaAndSpace(Row row, int colIndex) {
//        Cell cell = row.getCell(colIndex);
//        if (cell != null && cell.getCellType() == CellType.STRING) {
//            String cellValue = cell.getStringCellValue();
//            return cellValue.replace("[", "").replace("]", "").split("', '"); // Разделение по ", "
//        }
//        return new String[]{""}; // Если данных нет, возвращаем пустое значение
//    }
//
//    // Метод для копирования строки, исключая колонки N, P, U, V, W
//    private static void copyRow(Row sourceRow, Row targetRow) {
//        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
//            if (i != 26 && i != 27 && i != 28 && i != 29 && i != 30) { // Пропускаем колонки N, P, U, V, W
//                Cell sourceCell = sourceRow.getCell(i);
//                if (sourceCell != null) {
//                    Cell targetCell = targetRow.createCell(i);
//                    copyCell(sourceCell, targetCell);
//                }
//            }
//        }
//    }
//
//    // Метод для копирования данных ячейки
//    private static void copyCell(Cell sourceCell, Cell targetCell) {
//        switch (sourceCell.getCellType()) {
//            case STRING:
//                targetCell.setCellValue(sourceCell.getStringCellValue());
//                break;
//            case NUMERIC:
//                targetCell.setCellValue(sourceCell.getNumericCellValue());
//                break;
//            case BOOLEAN:
//                targetCell.setCellValue(sourceCell.getBooleanCellValue());
//                break;
//            case FORMULA:
//                targetCell.setCellFormula(sourceCell.getCellFormula());
//                break;
//            default:
//                break;
//        }
//    }
//
//    // Метод для выбора файла с помощью JFileChooser
//    private static String chooseFile(String dialogTitle) {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle(dialogTitle);
//        int result = fileChooser.showOpenDialog(null);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            return fileChooser.getSelectedFile().getAbsolutePath();
//        }
//        return null;
//    }
//
//    // Метод для выбора места сохранения файла
//    private static String chooseFileForSave(String dialogTitle) {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle(dialogTitle);
//        int result = fileChooser.showSaveDialog(null);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            return fileChooser.getSelectedFile().getAbsolutePath();
//        }
//        return null;
//    }
//}
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FedresursSearch {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FedresursSearch::createUI);
    }

    private static void createUI() {
        JFrame frame = new JFrame("Разделение данных в Excel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Поля для выбора исходного и сохраняемого файлов
        JLabel lblSourceFile = new JLabel("Исходный файл Excel:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(lblSourceFile, gbc);

        JTextField txtSourceFile = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(txtSourceFile, gbc);

        JButton btnSourceFile = new JButton("Выбрать...");
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(btnSourceFile, gbc);

        JLabel lblTargetFile = new JLabel("Сохраняемый файл Excel:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblTargetFile, gbc);

        JTextField txtTargetFile = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(txtTargetFile, gbc);

        JButton btnTargetFile = new JButton("Выбрать...");
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(btnTargetFile, gbc);

        // Выбор колонок для разделения по типу N
        JLabel lblNColumns = new JLabel("Выберите колонки для разделения по типу ',':");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        panel.add(lblNColumns, gbc);

        JPanel pnlNColumns = new JPanel(new GridLayout(0, 8));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.ipady = 40;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPaneN = new JScrollPane(pnlNColumns);
        panel.add(scrollPaneN, gbc);

        // Выбор колонок для разделения по типу P, U, V, W
        JLabel lblOtherColumns = new JLabel("Выберите колонки для разделения по типу ', ':");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(lblOtherColumns, gbc);

        JPanel pnlOtherColumns = new JPanel(new GridLayout(0, 8));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.ipady = 40;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane scrollPaneOther = new JScrollPane(pnlOtherColumns);
        panel.add(scrollPaneOther, gbc);

        JButton btnRun = new JButton("Запустить");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.ipady = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(btnRun, gbc);

        // Генерация списка колонок до "DD"
        String[] columns = generateExcelColumns(104);

        List<JCheckBox> nColumnCheckboxes = new ArrayList<>();
        List<JCheckBox> otherColumnCheckboxes = new ArrayList<>();

        for (String column : columns) {
            JCheckBox checkBoxN = new JCheckBox(column);
            nColumnCheckboxes.add(checkBoxN);
            pnlNColumns.add(checkBoxN);

            JCheckBox checkBoxOther = new JCheckBox(column);
            otherColumnCheckboxes.add(checkBoxOther);
            pnlOtherColumns.add(checkBoxOther);
        }

        btnSourceFile.addActionListener(e -> {
            String selectedFile = chooseFile("Выберите исходный Excel файл");
            txtSourceFile.setText(selectedFile);
        });

        btnTargetFile.addActionListener(e -> {
            String selectedFile = chooseFileForSave("Выберите файл для сохранения Excel");
            txtTargetFile.setText(selectedFile);
        });

        // Слушатель для кнопки "Запустить"
        btnRun.addActionListener(e -> {
            String sourcePath = txtSourceFile.getText();
            String targetPath = txtTargetFile.getText();

            if (sourcePath.isEmpty() || targetPath.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Пожалуйста, выберите исходный и сохраняемый файлы.");
                return;
            }

            // Получаем выбранные колонки
            List<String> selectedNColumns = new ArrayList<>();
            List<String> selectedOtherColumns = new ArrayList<>();

            for (JCheckBox checkBox : nColumnCheckboxes) {
                if (checkBox.isSelected()) {
                    selectedNColumns.add(checkBox.getText());
                }
            }

            for (JCheckBox checkBox : otherColumnCheckboxes) {
                if (checkBox.isSelected()) {
                    selectedOtherColumns.add(checkBox.getText());
                }
            }

            // Выполняем процесс разделения данных
            try {
                processExcelFile(sourcePath, targetPath, selectedNColumns, selectedOtherColumns);
                JOptionPane.showMessageDialog(frame, "Данные успешно обработаны и сохранены.");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Ошибка при обработке файла.");
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    // Генерация названий колонок до "DD"
    private static String[] generateExcelColumns(int limit) {
        List<String> columns = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            columns.add(convertToExcelColumn(i));
        }
        return columns.toArray(new String[0]);
    }

    // Преобразование числового индекса в буквенное обозначение колонки Excel
    private static String convertToExcelColumn(int index) {
        StringBuilder column = new StringBuilder();
        while (index >= 0) {
            column.insert(0, (char) ('A' + (index % 26)));
            index = index / 26 - 1;
        }
        return column.toString();
    }

    // Метод для выбора файла
    private static String chooseFile(String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(dialogTitle);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    // Метод для выбора файла сохранения
    private static String chooseFileForSave(String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(dialogTitle);
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    // Метод для обработки Excel файла
    private static void processExcelFile(String sourcePath, String targetPath, List<String> nColumns, List<String> otherColumns) throws IOException {
        FileInputStream fis = new FileInputStream(new File(sourcePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        Workbook newWorkbook = new XSSFWorkbook();
        Sheet newSheet = newWorkbook.createSheet("Разделенные данные");

        int newRowIdx = 0;

        for (Row row : sheet) {
            int maxSplits = 1;

            for (String colName : nColumns) {
                int colIndex = getExcelColumnIndex(colName);
                String[] splitData = getSplitDataByComma(row, colIndex);
                maxSplits = Math.max(maxSplits, splitData.length);
            }

            for (String colName : otherColumns) {
                int colIndex = getExcelColumnIndex(colName);
                String[] splitData = getSplitDataByCommaAndSpace(row, colIndex);
                maxSplits = Math.max(maxSplits, splitData.length);
            }

            for (int i = 0; i < maxSplits; i++) {
                Row newRow = newSheet.createRow(newRowIdx++);
                copyRow(row, newRow);

                for (String colName : nColumns) {
                    int colIndex = getExcelColumnIndex(colName);
                    String[] splitData = getSplitDataByComma(row, colIndex);
                    if (i < splitData.length) {
                        newRow.createCell(colIndex).setCellValue(splitData[i]);
                    }
                }

                for (String colName : otherColumns) {
                    int colIndex = getExcelColumnIndex(colName);
                    String[] splitData = getSplitDataByCommaAndSpace(row, colIndex);
                    if (i < splitData.length) {
                        newRow.createCell(colIndex).setCellValue(splitData[i]);
                    }
                }
            }
        }

        FileOutputStream fos = new FileOutputStream(new File(targetPath));
        newWorkbook.write(fos);
        fos.close();
        workbook.close();
        fis.close();
    }

    private static int getExcelColumnIndex(String columnName) {
        int result = 0;
        for (int i = 0; i < columnName.length(); i++) {
            result *= 26;
            result += columnName.charAt(i) - 'A' + 1;
        }
        return result - 1;
    }

    private static String[] getSplitDataByComma(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell != null && cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().split(",");
        }
        return new String[]{""};
    }

    private static String[] getSplitDataByCommaAndSpace(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell != null && cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().split("', '");
        }
        return new String[]{""};
    }

    private static void copyRow(Row sourceRow, Row targetRow) {
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell sourceCell = sourceRow.getCell(i);
            if (sourceCell != null) {
                Cell targetCell = targetRow.createCell(i);
                copyCell(sourceCell, targetCell);
            }
        }
    }

    private static void copyCell(Cell sourceCell, Cell targetCell) {
        switch (sourceCell.getCellType()) {
            case STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                targetCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            case BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            default:
                break;
        }
    }
}




