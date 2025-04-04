import requests

url = "http://localhost:8000/api/upload"
files = {'file': open("C:/Users/201-07/Desktop/pkh/final-project/fastapi/pdf/galaxy-s24-manual-SM-S92X-DS-UU-rev-1-Korean.pdf", "rb")}
data = {
    "collection_name": "langchain",
    "chunk_size": 210,
    "chunk_overlap": 50
}

response = requests.post(url, files=files, data=data)
print(response.json())
