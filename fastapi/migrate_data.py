# import os
# import psycopg2
# from dotenv import load_dotenv
# import json
# import uuid

# load_dotenv()

# def get_db_connection():
#     """데이터베이스 연결을 반환합니다."""
#     conn_string = f"postgresql://{os.getenv('POSTGRES_USER')}:{os.getenv('POSTGRES_PASSWORD')}@{os.getenv('POSTGRES_HOST')}:{os.getenv('POSTGRES_PORT')}/{os.getenv('POSTGRES_DB')}"
#     print(f"데이터베이스 연결 문자열: {conn_string}")
#     return psycopg2.connect(conn_string)

# def migrate_data():
#     """기존 데이터를 PGVector 형식으로 마이그레이션합니다."""
#     try:
#         conn = get_db_connection()
#         cursor = conn.cursor()

#         # 1. 컬렉션 생성
#         collection_uuid = str(uuid.uuid4())
#         print("컬렉션 생성 중...")
#         cursor.execute("""
#             INSERT INTO langchain_pg_collection (uuid, name, cmetadata)
#             VALUES (%s, %s, %s)
#         """, (collection_uuid, "embedding", json.dumps({})))
#         print("컬렉션 생성 완료")

#         # 2. 기존 데이터 조회
#         print("기존 데이터 조회 중...")
#         cursor.execute("""
#             SELECT id, product_id, embedding, document, cmetadata 
#             FROM embedding
#         """)
#         rows = cursor.fetchall()
#         print(f"조회된 데이터 개수: {len(rows)}")

#         # 3. 데이터 마이그레이션
#         for row in rows:
#             id, product_id, embedding, document, cmetadata = row
#             print(f"데이터 처리 중: id={id}, product_id={product_id}")
            
#             # metadata JSONB 생성
#             metadata = {}
#             if cmetadata:
#                 try:
#                     metadata = json.loads(cmetadata)
#                 except:
#                     metadata = {}
            
#             # product_id를 metadata에 추가
#             metadata['product_id'] = product_id
            
#             # 새로운 테이블에 데이터 삽입
#             cursor.execute("""
#                 INSERT INTO langchain_pg_embedding (
#                     id, 
#                     collection_id, 
#                     embedding, 
#                     document, 
#                     metadata
#                 )
#                 VALUES (%s, %s, %s, %s, %s)
#             """, (id, collection_uuid, embedding, document, json.dumps(metadata)))

#         conn.commit()
#         print(f"총 {len(rows)}개의 데이터가 마이그레이션되었습니다.")

#     except Exception as e:
#         print(f"데이터 마이그레이션 중 오류 발생: {e}")
#         import traceback
#         print(f"DEBUG: 상세 오류 - {traceback.format_exc()}")
#         conn.rollback()
#     finally:
#         cursor.close()
#         conn.close()

# if __name__ == "__main__":
#     migrate_data() 