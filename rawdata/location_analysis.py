import pandas as pd
from collections import Counter

def analyze_location_distribution(csv_file):
    try:
        # 讀取 CSV 文件，使用斜線作為分隔符
        df = pd.read_csv(csv_file, sep='/', header=0)
        
        # 重命名欄位
        df.columns = ['city', 'town', 'latitude', 'longitude']
        
        # 計算城市分布
        city_counts = df['city'].value_counts()
        
        # 計算區域分布
        town_counts = df['town'].value_counts()
        
        # 按城市統計區域分布
        city_town_groups = df.groupby(['city', 'town']).size()
        
        # 輸出結果
        print("\n城市分布統計:")
        print("-" * 30)
        for city, count in city_counts.items():
            print(f"{city}: {count:,} 筆")
        
        print("\n各城市的區域分布:")
        print("-" * 30)
        for city in city_counts.index:
            print(f"\n{city}:")
            city_data = df[df['city'] == city]
            town_dist = city_data['town'].value_counts()
            for town, count in town_dist.items():
                print(f"  {town}: {count:,} 筆")
        
        # 輸出總計
        print("\n總計統計:")
        print("-" * 30)
        print(f"總資料筆數: {len(df):,}")
        print(f"不同城市數量: {len(city_counts):,}")
        print(f"不同區域數量: {len(town_counts):,}")
        
        return {
            'city_counts': city_counts,
            'town_counts': town_counts,
            'city_town_groups': city_town_groups
        }
        
    except Exception as e:
        print(f"處理資料時發生錯誤: {str(e)}")
        return None

# 使用範例
if __name__ == "__main__":
    csv_file = "input/geoGusser.csv"  # 請替換成你的 CSV 檔案路徑
    results = analyze_location_distribution(csv_file)