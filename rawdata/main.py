import pandas as pd
import os
import re
import json

def load_mapping_files():
    """載入城市和行政區對照表"""
    try:
        with open('mapping/city_mapping.json', 'r', encoding='utf-8') as f:
            city_mapping = json.load(f)
        with open('mapping/town_mapping.json', 'r', encoding='utf-8') as f:
            town_mapping = json.load(f)
        return city_mapping, town_mapping
    except FileNotFoundError as e:
        print(f"無法找到對照表檔案: {str(e)}")
        return {}, {}
    except json.JSONDecodeError as e:
        print(f"JSON 檔案格式錯誤: {str(e)}")
        return {}, {}

def get_english_name(chinese_name):
    """取得城市英文名稱"""
    city_mapping, _ = load_mapping_files()
    return city_mapping.get(chinese_name, chinese_name)

def get_town_english_name(chinese_town):
    """取得行政區英文名稱"""
    _, town_mapping = load_mapping_files()
    
    # 移除尾部的行政區類型標識
    base_name = re.sub(r'[區鄉鎮市]$', '', chinese_town)
    
    # 如果有對應的英文名稱就使用，否則用原始名稱
    english_name = town_mapping.get(base_name, base_name)
    
    # 根據原始名稱的尾部添加對應的英文後綴
    # if '區' in chinese_town:
    #     return f"{english_name}_District"
    # elif '鄉' in chinese_town:
    #     return f"{english_name}_Township"
    # elif '鎮' in chinese_town:
    #     return f"{english_name}_Town"
    # elif '市' in chinese_town and len(chinese_town) <= 3:  # 確保是行政區的"市"，而不是縣市的"市"
    #     return f"{english_name}_City"
    # else:
    #     return english_name

def split_data_into_csvs(input_file):
    try:
        # 讀取原始資料
        df = pd.read_csv(input_file, sep='/')
        df.columns = ['city', 'town', 'latitude', 'longitude']
        
        # 創建輸出目錄
        output_dir = 'split_data_city'
        if not os.path.exists(output_dir):
            os.makedirs(output_dir)
        
        # 取得所有不重複的城市名稱
        cities = df['city'].unique()
        
        # 為每個城市建立獨立的CSV檔案
        for city in cities:
            # 篩選該城市的資料
            city_data = df[df['city'] == city][['town', 'latitude', 'longitude']]
            
            # 取得英文檔名
            filename = get_english_name(city)
            
            # 存檔
            city_data.to_csv(f'{output_dir}/{filename}.csv', 
                           index=False, 
                           encoding='utf-8-sig')
            
            # 輸出處理進度
            print(f'已處理 {city}({filename})：{len(city_data)} 筆資料')
        
        print('\n資料處理完成！')
        print(f'檔案已儲存在 {output_dir} 目錄下')
        print(f'共處理 {len(cities)} 個城市')
        
    except Exception as e:
        print(f'處理資料時發生錯誤：{str(e)}')

if __name__ == "__main__":
    input_file = "input/geoGusser.csv"  # 請替換成你的輸入檔案名稱
    split_data_into_csvs(input_file)