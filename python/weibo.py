import requests
from urllib.parse import urlencode
import time
import json

# 用户ID（铁路上海站的微博ID）
user_id = "1917205532"

# 请求头（需替换成你的浏览器信息）
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36",
    "Cookie": "mycookie",  # 需自行登录后获取
    "Referer": f"https://weibo.com/u/{user_id}",
}

# 配置
LONGTEXT_URL = "https://weibo.com/ajax/statuses/longtext"

def get_longtext_content(weibo_id):
    """获取微博长文本内容"""
    params = {
        "id": weibo_id,  # 微博ID
    }
    try:
        # 发送请求
        response = requests.get(LONGTEXT_URL, headers=headers, params=params)
        response.raise_for_status()  # 检查请求是否成功

        # 设置响应编码为 UTF-8
        response.encoding = "utf-8"

        # 解析 JSON 数据
        data = response.json()
        longtext_content = data.get("data", {}).get("longTextContent", "")
        return longtext_content
    except requests.exceptions.RequestException as e:
        print(f"请求失败：{e}")
        return None

# 微博API接口（通过浏览器开发者工具获取）
def get_weibo_list(user_id, page=1):
    url = "https://weibo.com/ajax/statuses/mymblog"
    params = {
        "uid": user_id,
        "page": page,
        "feature": "0",
    }
    response = requests.get(url, headers=headers, params=params)
    if response.status_code == 200:
        return response.json()
    else:
        print(f"请求失败，状态码：{response.status_code}")
        return None

# 解析数据
def parse_weibo_data(data):
    weibo_list = []
    for item in data.get("data", {}).get("list", []):
        weibo_text = item.get("text_raw")

        if '#运输资讯#' not in weibo_text:
            continue

        longtext_content = get_longtext_content(item.get("mblogid"))
        longtext_content = longtext_content.replace("上海", "\t上海")
        longtext_content = longtext_content.replace("站", "站\t")
        longtext_content = longtext_content.replace("、", "\t")
        longtext_content = longtext_content.replace("，", "\t")
        longtext_content = longtext_content.replace("今日是", "今日是\t")
        longtext_content = longtext_content.replace("万", "\t万")
        longtext_content = longtext_content.replace("旅客", "旅客\t")
        
        print(longtext_content)
        weibo = {
            "id": item.get("id"),
            "text": longtext_content,
            "created_at": item.get("created_at"),
            # "reposts_count": item.get("reposts_count"),
            # "comments_count": item.get("comments_count"),
            # "attitudes_count": item.get("attitudes_count"),
        }
        weibo_list.append(weibo)
    return weibo_list



if __name__ == '__main__':
    with open("D:\\dev\\weibo\\output.txt", mode="w", newline="", encoding="utf-8") as file:
        for page_index in range(1000):
            # 示例：获取第一页数据
            print(page_index)
            data = get_weibo_list(user_id, page=page_index)
            if data:
                weibo_data = parse_weibo_data(data)
                for line in weibo_data:
                    file.write(line["text"])
                    file.write("\n")

                # print(json.dumps(weibo_data, indent=2, ensure_ascii=False))
