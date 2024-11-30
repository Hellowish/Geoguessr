-- 使用 geoguessr 資料庫
USE geoguessr;

-- 建立名為 all_city_town_mapping 的資料表
CREATE TABLE all_city_town_mapping (
    id INT AUTO_INCREMENT PRIMARY KEY, -- 自動遞增主鍵
    column1 VARCHAR(255),              -- 替換為實際欄位名稱
    column2 VARCHAR(255),              -- 替換為實際欄位名稱
    column3 VARCHAR(255)               -- 替換為實際欄位名稱（根據 CSV 的結構）
);

-- 匯入資料到 all_city_town_mapping 表格
LOAD DATA INFILE 'C:/Users/Administrator/Desktop/rawdata/split_data_city/all_city_town_mapping.csv'
INTO TABLE all_city_town_mapping
FIELDS TERMINATED BY ','              -- 以逗號分隔欄位
LINES TERMINATED BY '\n'              -- 每行為一筆資料
IGNORE 1 ROWS                         -- 忽略第一行（表頭）
(column1, column2, column3);          -- 替換為實際欄位名稱
