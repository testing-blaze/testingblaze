/*
 * Copyright 2020
 *
 * This file is part of  Test Blaze Bdd Framework [Test Blaze Automation Solution].
 *
 * Test Blaze Bdd Framework is licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy
 * of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.testblaze.misclib;

import com.testblaze.controller.DeviceBucket;
import com.testblaze.objects.InstanceRecording;
import com.testblaze.register.i;
import com.testblaze.report.LogLevel;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Random;

public final class FileHandler {
    private ExcelReader excelReader;
    private JsonReader jsonReader;
    private AdobeReader adobeReader;
    private WebDriver driver;

    public FileHandler() {
        this.excelReader = new ExcelReader();
        this.jsonReader = new JsonReader();
        this.adobeReader = new AdobeReader();
        this.driver = InstanceRecording.getInstance(DeviceBucket.class).getDriver();
    }

    /**
     * perform various functions on Excel files
     *
     * @return
     * @author nauman.shahid
     */
    public ExcelReader excel() {
        return this.excelReader;
    }

    /**
     * perform various functions on Json files
     *
     * @return
     * @author nauman.shahid
     */
    public JsonReader json() {
        return this.jsonReader;
    }

    /**
     * Perform various operations onn adob files
     *
     * @return
     * @author nauman.shahid
     */
    public AdobeReader adobeReader() {
        return this.adobeReader;
    }

    /**
     * Provide complete file path with fileName and extension. send as absolute path
     *
     * @param filePathWithFileName
     * @return String Array list of lines
     * @author nauman.shahid
     */
    public List<String> readAnyFile(String filePathWithFileName) {
        List<String> fileData = null;
        try {
            fileData = Files.readAllLines(Paths.get(filePathWithFileName));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileData;
    }

    /**
     * get list of all files from a local directory
     *
     * @param path
     * @return directory
     * @author nauman.shahid
     */
    public File[] getCompleteFilesListOnLocalDirectory(String path) {
        File directory = new File(path);
        return directory.listFiles();
    }

    /**
     * rename any downloaded file with .tmp extension
     *
     * @param file     directory path
     * @param fileName new name of file
     * @param fileType new extension of file
     * @author nauman.shahid
     */
    public void reNameDownloadedTmpFile(File file, String fileName, String fileType) {
        File[] directory = getCompleteFilesListOnLocalDirectory(file.getAbsolutePath());
        for (File files : directory) {
            if (files.getName().endsWith(".tmp")) {
                new File(files.getAbsolutePath())
                        .renameTo(new File(file.getAbsolutePath() + "\\" + fileName + "." + fileType));
                break;
            }
        }
    }

    /**
     * create any file
     *
     * @param pathWithFileName path with filename and extension /abc/file.txt
     * @author nauman.shahid
     */
    public void createFile(String pathWithFileName) {
        try {
            if (Files.notExists(Paths.get(pathWithFileName))) ;
            Files.createFile(Paths.get(pathWithFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Write to an existing or new file
     *
     * @param pathWithFileName path with filename and extension /abc/file.txt
     * @param dataToWrite
     * @author nauman.shahid
     */
    public void writeInFile(String pathWithFileName, String dataToWrite) {
        try {
            if (Files.notExists(Paths.get(pathWithFileName)))
                Files.write(Paths.get(pathWithFileName), dataToWrite.getBytes());
            else
                Files.write(Paths.get(pathWithFileName), dataToWrite.getBytes(), StandardOpenOption.APPEND);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create a new folder
     *
     * @param pathOfDirectoryName path with filename and extension /abc/folderName
     * @author nauman.shahid
     */
    public void createDirectory(String pathOfDirectoryName) {
        if (Files.notExists(Paths.get(pathOfDirectoryName))) {
            try {
                Files.createDirectory(Paths.get(pathOfDirectoryName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles all methods related to excel files
     *
     * @author nauman.shahid
     */
    public final class ExcelReader {

        public XSSFWorkbook workbook;
        public XSSFSheet sheet;
        public XSSFRow row;
        public XSSFCell cell;

        /**
         * Enter Filename with extension and sheet name. Place file in resources folder
         * of project.
         *
         * @param fileName
         * @param sheetName
         * @return String array of data
         * @author nauman.shahid
         */
        public String[][] readExcelFile(String fileName, String sheetName) {
            try {
                InputStream is = getClass().getResourceAsStream("/" + fileName);
                workbook = new XSSFWorkbook(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getDataFromSheet(sheetName);
        }

        /**
         * Read downloaded Excel file at default location /target
         *
         * @param fileName  name of file with extension
         * @param sheetName
         * @return string[][]
         * @author nauman.shahid
         */
        public String[][] readFromDownloadedFile(String fileName, String sheetName) {
            File file = new File(System.getProperty("user.dir") + "\\target");
            reNameDownloadedTmpFile(file, fileName, "xlsx");
            return readExcelFile(fileName, sheetName,
                    file.getAbsolutePath() + "\\" + fileName);
        }

        /**
         * Enter file name, sheet name and custom path as well to read file.
         *
         * @param fileName
         * @param sheetName
         * @param filePath
         * @return string array.
         * @author nauman.shahid
         */
        public String[][] readExcelFile(String fileName, String sheetName, String filePath) {
            try {
                InputStream is = new FileInputStream(filePath + "/" + fileName);
                workbook = new XSSFWorkbook(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getDataFromSheet(sheetName);
        }

        private String[][] getDataFromSheet(String sheetName) {
            String[][] dataSets = null;
            try {
                XSSFSheet sheet = workbook.getSheet(sheetName);
                int totalRow = sheet.getLastRowNum() + 1;
                int totalColumn = sheet.getRow(0).getLastCellNum();
                dataSets = new String[totalRow - 1][totalColumn];
                for (int i = 1; i < totalRow; i++) {
                    XSSFRow rows = sheet.getRow(i);
                    for (int j = 0; j < totalColumn; j++) {
                        XSSFCell cell = rows.getCell(j);
                        if (cell.getCellTypeEnum() == CellType.STRING)
                            dataSets[i - 1][j] = cell.getStringCellValue();
                        else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                            long cellText = Math.round(cell.getNumericCellValue());
                            String cellTextString = Long.toString(cellText);
                            dataSets[i - 1][j] = cellTextString;
                        } else
                            dataSets[i - 1][j] = String.valueOf(cell.getBooleanCellValue());
                    }
                }

            } catch (Exception e) {
                //Console log Exception in reading Xlxs file" + e.getMessage());
                e.printStackTrace();
            }
            return dataSets;
        }

        /**
         * Parse any file to an excel file
         *
         * @param fileToBeParsedPath           full path of the file with file name and
         *                                     extension
         * @param afterParsingFilePathWithName full path of the new file with name and
         *                                     extension
         * @author nauman.shahid
         */

        public void parseAnyFileAsExcel(String fileToBeParsedPath, String afterParsingFilePathWithName) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(new File(fileToBeParsedPath)));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            XSSFWorkbook xwork = new XSSFWorkbook();
            XSSFSheet xsheet = xwork.createSheet("testBlaze");
            XSSFRow xrow = null;

            int rowid = 0;
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    String[] split = line.split("<br>");
                    Cell cell;
                    for (int i = 0; i < split.length; i++) {
                        xrow = xsheet.createRow(rowid);
                        cell = xrow.createCell(2);
                        cell.setCellValue(split[i]);
                        String[] columnSplit = split[i].split("\\W+");
                        int columnCount = 3;
                        for (int j = 0; j < columnSplit.length; j++) {

                            cell = xrow.createCell(columnCount++);
                            cell.setCellValue(columnSplit[j]);
                        }

                        rowid++;
                    }

                    // Create file system using specific name
                    FileOutputStream fout = new FileOutputStream(new File(afterParsingFilePathWithName));
                    xwork.write(fout);
                    fout.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Console log  File Parsing process completed");
        }
    }

    /**
     * Handle various json related functions
     *
     * @author nauman.shahid
     */
    public final class JsonReader {
        InputStream is;
        Reader reader;
        JsonParser jsonParser;

        /**
         * read data with title
         *
         * @param fileName
         * @param childObject
         * @param key
         * @return
         */
        public String getDataFromJson(String fileName, String childObject, String key) {
            setUpReadStream(fileName);
            // getting the root element - the whole set of json data
            JsonElement rootElement = jsonParser.parse(reader);
            // covert it to the json object to access different child objects and arrays
            JsonObject theObject = rootElement.getAsJsonObject();
            return theObject.getAsJsonObject(childObject).get(key).getAsString();
        }

        /**
         * getJsonData
         *
         * @param fileName
         * @return
         */
        public JsonElement getDataFromJson(String fileName) {
            setUpReadStream(fileName);
            // getting the root element - the whole set of json data
            JsonElement rootElement = jsonParser.parse(reader);
            // covert it to the json object to access different child objects and arrays
            return rootElement.getAsJsonObject();
        }

        /**
         * read data with multiple titles
         *
         * @param fileName
         * @param firstChildObject
         * @param secondChildObject
         * @param key
         * @return
         */
        public String getDataFromJson(String fileName, String firstChildObject, String secondChildObject, String key) {
            setUpReadStream(fileName);
            // getting the root element - the whole set of json data
            JsonElement rootElement = jsonParser.parse(reader);
            // covert it to the json object to access different child objects and arrays
            JsonObject theObject = rootElement.getAsJsonObject();
            JsonObject childObject = theObject.getAsJsonObject(firstChildObject).getAsJsonObject(secondChildObject);
            return childObject.get(key).toString();
        }

        /**
         * read data from array
         *
         * @param fileName
         * @param firstChildObject
         * @param secondChildObject
         * @param arrayName
         * @param key
         * @return
         */
        public String getDataFromJson(String fileName, String firstChildObject, String secondChildObject,
                                      String arrayName, String key) {
            setUpReadStream(fileName);
            // getting the root element - the whole set of json data
            JsonElement rootElement = jsonParser.parse(reader);
            // covert it to the json object to access different child objects and arrays
            JsonObject theObject = rootElement.getAsJsonObject();
            JsonObject childObject = theObject.getAsJsonObject(firstChildObject).getAsJsonObject(secondChildObject);
            return childObject.getAsJsonArray(arrayName).getAsJsonObject().get(key).toString();
        }

        /**
         * get object array
         *
         * @param fileName
         * @param firstChildObject
         * @param secondChildObject
         * @param arrayName
         * @param key
         * @return
         */
        public JsonObject getDataArrayFromJson(String fileName, String firstChildObject, String secondChildObject,
                                               String arrayName, String key) {
            setUpReadStream(fileName);
            // getting the root element - the whole set of json data
            JsonElement rootElement = jsonParser.parse(reader);
            // covert it to the json object to access different child objects and arrays
            JsonObject theObject = rootElement.getAsJsonObject();
            JsonObject childObject = theObject.getAsJsonObject(firstChildObject).getAsJsonObject(secondChildObject);
            return childObject.getAsJsonArray(arrayName).getAsJsonObject();
        }

        /**
         * maange instance creation and input stream
         *
         * @param fileName
         */
        private void setUpReadStream(String fileName) {
            is = getClass().getClassLoader().getResourceAsStream(fileName);
            reader = new InputStreamReader(is);
            getJsonParserInstance();
        }

        /**
         * manage instance for json parser
         */
        private void getJsonParserInstance() {
            if (jsonParser == null) {
                jsonParser = new JsonParser();
            }
        }
    }


    /**
     * Read Adobe files
     *
     * @author nauman.shahid
     */
    public final class AdobeReader {
        InputStream is;
        Reader reader;

        /**
         * Read downloaded pdf file with extension at default location /target
         *
         * @param fileName   name of file
         * @param pageNumber
         * @return string
         * @author nauman.shahid
         */
        public String readFromDownloadedFile(String fileName, int pageNumber) {
            File file = new File(System.getProperty("user.dir") + "\\target");
            reNameDownloadedTmpFile(file, fileName, "pdf");
            return readFromAdobeFileOnLocalAtUserDirectory(
                    file.getAbsolutePath() + "\\" + fileName, pageNumber);
        }


        /**
         * read a file from src/main/resources
         *
         * @param fileName
         * @param pageNumber
         * @return string after readinf file
         * @author nauman.shahid
         */
        public String readFromAdobeFileFromResources(String fileName, int pageNumber) {
            return adobeReader(fileName, pageNumber);
        }

        /**
         * read file from url
         *
         * @param url
         * @param pageNumber
         * @return string after reading file
         * @throws MalformedURLException
         * @throws FileNotFoundException
         * @author nauman.shahid
         */
        public String readFromAdobeFileAfterDownloading(String url, int pageNumber)
                throws MalformedURLException, FileNotFoundException {
            return adobeReaderFromURL(url, pageNumber);
        }

        /**
         * read file from a specific location
         *
         * @param fileName
         * @param pageNumber
         * @return file after reading in string
         * @author nauman.shahid
         */
        public String readFromAdobeFileOnLocalAtUserDirectory(String fileName, int pageNumber) {
            return adobeReaderAtSpecificLocation(fileName, pageNumber);
        }

        /**
         * manage instance creation
         *
         * @param fileName
         * @return
         * @author nauman.shahid
         */
        private InputStream setUpReadStream(String fileName) {
            return is = getClass().getClassLoader().getResourceAsStream("/" + fileName);
        }

        /**
         * @param fileName
         * @param pageNumber
         * @return
         * @author nauman.shahid
         */
        private String adobeReader(String fileName, int pageNumber) {
            String page = null;
            try {
                PdfReader reader = new PdfReader(setUpReadStream(fileName));
                //Console log This PDF has " + reader.getNumberOfPages() + " pages.");
                page = PdfTextExtractor.getTextFromPage(reader, pageNumber);
                //Console log Page Content:\n\n" + page + "\n\n");
                //Console log Is this document tampered: " + reader.isTampered());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return page;
        }

        /**
         * @param path
         * @param pageNumber
         * @return
         * @author nauman.shahid
         */
        private String adobeReaderAtSpecificLocation(String path, int pageNumber) {
            String page = null;
            try {
                PdfReader reader = new PdfReader(path);
                //Console log This PDF has " + reader.getNumberOfPages() + " pages.");
                page = PdfTextExtractor.getTextFromPage(reader, pageNumber);
                //Console log Page Content:\n\n" + page + "\n\n");
                //Console log  Is this document tampered: " + reader.isTampered());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return page;
        }

        /**
         * @param url
         * @param pageNumber
         * @return
         * @throws MalformedURLException
         * @throws FileNotFoundException
         * @author nauman.shahid
         */
        private String adobeReaderFromURL(String url, int pageNumber)
                throws MalformedURLException, FileNotFoundException {
            String page = null;
            try {
                PdfReader reader = new PdfReader(setUpReadStream(downloadURLBasePDF(url)));
                //Console log This PDF has " + reader.getNumberOfPages() + " pages.");
                page = PdfTextExtractor.getTextFromPage(reader, pageNumber);
                //Console log Page Content:\n\n" + page + "\n\n");
                //Console log Is this document tampered: " + reader.isTampered());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return page;
        }

        /**
         * @param url
         * @return
         * @throws MalformedURLException
         * @throws FileNotFoundException
         * @author nauman.shahid
         */
        private String downloadURLBasePDF(String url) throws MalformedURLException, FileNotFoundException {
            Random random = new Random(500);
            String newFileName = "newPdfFile" + random + ".pdf";
            URL url1 = new URL(url);
            File file = new File(System.getProperty("user.dir") + "src/main/resources", newFileName);
            byte[] ba1 = new byte[1024];
            int baLength;
            FileOutputStream fos1 = new FileOutputStream(file);

            try {
                // Contacting the URL
                i.amPerforming().updatingReportWith().write(LogLevel.TEST_BLAZE_INFO, "Connecting to " + url1.toString() + " ... ");
                URLConnection urlConn = url1.openConnection();

                // Checking whether the URL contains a PDF
                if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
                    //Console log FAILED.\n[Sorry. This is not a PDF.]");
                } else {
                    try {

                        // Read the PDF from the URL and save to a local file
                        InputStream is1 = url1.openStream();
                        while ((baLength = is1.read(ba1)) != -1) {
                            fos1.write(ba1, 0, baLength);
                        }
                        fos1.flush();
                        fos1.close();
                        is1.close();
                    } catch (Exception e) {

                    }
                }
            } catch (Exception e) {

            }
            return newFileName;
        }

    }

}