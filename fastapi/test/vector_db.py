from langchain_community.vectorstores import PGVector
from langchain_openai import OpenAIEmbeddings
import psycopg2
from dotenv import load_dotenv
import os

load_dotenv()


def get_db_connection_string():
    """환경에 따른 적절한 DB 연결 문자열 반환"""
    environment = os.getenv("PRODUCTION", "local").lower()
    env_type = "PRODUCTION" if environment == "production" else "LOCAL"
    conn_string = os.getenv(f"{env_type}_PGVECTOR_CONNECTION_STRING")
    if not conn_string:
        raise ValueError("DB 연결 문자열을 찾을 수 없습니다.")
    return conn_string


# def get_embeddings():
#     """OpenAI 임베딩 모델 인스턴스 반환"""
#     return OpenAIEmbeddings(model="text-embedding-3-small")


# def generate_collection_name(pdf_path):
#     """PDF 파일 경로에서 확장자 제외한 파일명 추출"""
#     return os.path.splitext(os.path.basename(pdf_path))[0]


################벡터스토어######################################


# def store_document_embeddings(document_chunks, file_path, product_id):
#     """문서를 벡터화하여 데이터베이스에 저장"""
#     try:
#         # 벡터스토어 컬렉션 이름 설정
#         collection_name = f"product_{product_id}_embeddings"

#         # 벡터스토어 생성 및 문서 추가
#         vector_store = PGVector(
#             collection_name=collection_name,
#             connection_string=get_db_connection_string(),
#             embedding_function=get_embeddings(),
#             # use_jsonb_metadata=True,
#         )

#         vector_store.add_documents(document_chunks)

#         # 제품 테이블에 제품 ID와 대표 임베딩 저장
#         # (대표 임베딩은 첫 번째 문서의 임베딩을 사용)
#         if document_chunks:
#             conn = psycopg2.connect(get_db_connection_string())
#             cursor = conn.cursor()

#             # 첫 번째 문서의 임베딩 생성
#             embeddings = get_embeddings()
#             embedding_vector = embeddings.embed_query(document_chunks[0].page_content)

#             # 제품 테이블에 product_id와 embedding 저장
#             # (제품이 이미 존재하면 임베딩만 업데이트)
#             cursor.execute(
#                 """
#                 INSERT INTO product (product_id, embedding, collection_name) 
#                 VALUES (%s, %s, %s)
#                 ON CONFLICT (product_id) 
#                 DO UPDATE SET embedding = %s, collection_name = %s
#                 """,
#                 (
#                     product_id,
#                     embedding_vector,
#                     collection_name,
#                     embedding_vector,
#                     collection_name,
#                 ),
#             )

#             conn.commit()
#             cursor.close()
#             conn.close()

#         print(
#             f"제품 ID {product_id}의 {len(document_chunks)}개 문서가 성공적으로 저장되었습니다."
#         )
#         return vector_store

#     except Exception as e:
#         print(f"문서 저장 중 오류 발생: {e}")
#         import traceback

#         traceback.print_exc()
#         return None


################################# 조회########################################


# def get_all_collections():
#     """데이터베이스에서 모든 컬렉션 목록 조회"""
#     try:
#         print("DEBUG: DB 연결 문자열 가져오기 시도")
#         # conn_string = get_db_connection_string()
#         conn_string = "postgresql://postgres:1234@localhost:5432/gigigenie"
#         print(f"DEBUG: 연결 문자열 - {conn_string}")

#         # 연결 수립
#         print("DEBUG: DB 연결 시도")
#         conn = psycopg2.connect(conn_string)
#         cursor = conn.cursor()
#         print("DEBUG: DB 연결 성공")

#         # SELECT table_name: 테이블 이름을 선택합니다.
#         # FROM information_schema.tables: 데이터베이스의 시스템 테이블 정보를 조회합니다.
#         # WHERE table_schema = 'public': 'public' 스키마의 테이블만 선택합니다.
#         # ORDER BY table_name: 테이블 이름을 알파벳 순으로 정렬합니다.
#         query = """
#         SELECT table_name FROM information_schema.tables 
#         WHERE table_schema = 'public'
#         ORDER BY table_name
#         """
#         # cursor.execute(query)는 SQL 쿼리를 실행하는 함수입니다.
#         print(f"DEBUG: SQL 쿼리 실행 - {query}")
#         cursor.execute(query)

#         # 결과에서 테이블 이름 추출
#         tables = cursor.fetchall()
#         print(f"DEBUG: 테이블 조회 결과 - {tables}")

#         # 테이블 이름 추출
#         collections = [row[0] for row in tables]
#         print(f"DEBUG: 추출된 컬렉션 - {collections}")

#         cursor.close()
#         conn.close()

#         # ['category', 'embedding', 'favorite', 'langchain_pg_collection', 'langchain_pg_embedding', 'member', 'product', 'query_history']
#         return collections

#     except Exception as e:
#         print(f"컬렉션 목록 조회 중 오류 발생: {e}")
#         import traceback

#         print(f"DEBUG: 상세 오류 - {traceback.format_exc()}")
#         return []


# def get_all_product_table():
#     """Product 테이블에 접근하여 모든 제품 정보를 조회합니다."""
#     try:
#         conn_string = "postgresql://postgres:1234@localhost:5432/gigigenie"
#         print("DEBUG: Product 테이블 연결 시도")
#         conn = psycopg2.connect(conn_string)
#         cursor = conn.cursor()
#         print("DEBUG: DB 연결 성공")

#         query = """
#         SELECT * FROM product
#         """
#         print(f"DEBUG: SQL 쿼리 실행 - {query}")
#         cursor.execute(query)

#         products = cursor.fetchall()
#         print(f"DEBUG: 제품 데이터 조회 결과 - {products}")

#         cursor.close()
#         conn.close()

#         return products

#     except Exception as e:
#         print(f"제품 조회 중 오류 발생: {e}")
#         import traceback

#         print(f"DEBUG: 상세 오류 - {traceback.format_exc()}")
#         return []


# Product 테이블 조회 테스트
# products = get_product_table()
# for product in products:
#     print("Product 테이블 데이터 - ", product)


# def get_product_by_id(id):
#     """특정 product_id로 제품 정보를 조회합니다."""
#     try:
#         conn_string = "postgresql://postgres:1234@localhost:5432/gigigenie"
#         print(f"DEBUG: Product ID {id} 조회 시도")
#         conn = psycopg2.connect(conn_string)
#         cursor = conn.cursor()
#         print("DEBUG: DB 연결 성공")

#         query = """
#         SELECT * FROM product WHERE product_id = %s
#         """
#         print(f"DEBUG: SQL 쿼리 실행 - {query}")
#         cursor.execute(query, (id,))

#         product = cursor.fetchone()
#         print(f"DEBUG: 제품 조회 결과 - {product}")

#         cursor.close()
#         conn.close()

#         return product

#     except Exception as e:
#         print(f"제품 ID 조회 중 오류 발생: {e}")
#         import traceback

#         print(f"DEBUG: 상세 오류 - {traceback.format_exc()}")
#         return []


# def get_all_product_ids():
#     """모든 제품의 ID 목록을 조회합니다."""
#     try:
#         conn_string = "postgresql://postgres:1234@localhost:5432/gigigenie"
#         print("DEBUG: 모든 Product ID 조회 시도")
#         conn = psycopg2.connect(conn_string)
#         cursor = conn.cursor()
#         print("DEBUG: DB 연결 성공")

#         query = """
#         SELECT product_id FROM product
#         """
#         print(f"DEBUG: SQL 쿼리 실행 - {query}")
#         cursor.execute(query)

#         product_ids = [row[0] for row in cursor.fetchall()]
#         print(f"DEBUG: 제품 ID 목록 - {product_ids}")

#         cursor.close()
#         conn.close()

#         return product_ids

#     except Exception as e:
#         print(f"제품 ID 목록 조회 중 오류 발생: {e}")
#         import traceback

#         print(f"DEBUG: 상세 오류 - {traceback.format_exc()}")
#         return []


################################# 리트리버 ########################################


def get_retriever(id, k=5):
    """벡터 스토어 기반 리트리버 생성"""
    try:
        conn_string = "postgresql://postgres:1005@localhost:5432/gigigenie"
        print(f"DEBUG: 데이터베이스 연결 시도 - {conn_string}")
        
        conn = psycopg2.connect(conn_string)
        cursor = conn.cursor()

        # 임베딩 데이터 조회
        query = """
        SELECT embedding, cmetadata 
        FROM langchain_pg_embedding 
        WHERE (cmetadata->>'product_id')::int = %s
        """
        print(f"DEBUG: 쿼리 실행 - {query} with id={id}")
        cursor.execute(query, (id,))
        result = cursor.fetchone()
        
        print(f"DEBUG: 쿼리 결과 - {result is not None}")
        
        if result:
            embedding_vector = result[0]
            metadata = result[1]
            print(f"DEBUG: 메타데이터 - {metadata}")
            
            # OpenAI 임베딩 객체 생성
            embeddings = OpenAIEmbeddings(model="text-embedding-3-small")
            
            collection_name = f"product_{id}_embeddings"
            vector_store = PGVector.from_existing_index(
                embedding=embeddings,
                collection_name=collection_name,
                connection_string=conn_string,
                embedding_function=embeddings,
            )
            print("DEBUG: 벡터 스토어 생성 완료")
            return vector_store.as_retriever(search_kwargs={"k": k})
        else:
            print(f"Product ID {id}에 대한 임베딩을 찾을 수 없습니다.")
            return None

    except Exception as e:
        print(f"오류 발생: {str(e)}")
        import traceback
        print(f"상세 오류: {traceback.format_exc()}")
        return None
    finally:
        if 'cursor' in locals():
            cursor.close()
        if 'conn' in locals():
            conn.close()


# 모든 제품 조회
# products = get_all_product_table()å


# # 첫 번째 제품 ID로 제품 조회 (ID가 있는 경우)
# product_id = 1
# product_id = get_product_by_id(product_id)
# prinfirst(f"제품 ID {product_id[0]}의 상세 정보:", product_id)

# # 모든 제품 ID 조회
# product_ids = get_all_product_ids()


# print(get_retriever(1))
