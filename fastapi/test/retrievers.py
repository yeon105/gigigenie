# from langchain_openai import ChatOpenAI
# from langchain.prompts import ChatPromptTemplate
# from langchain.schema.runnable import RunnablePassthrough
# from langchain.schema.output_parser import StrOutputParser
# from vector_db import get_retriever
# import os

# openai_api_key = os.getenv("OPENAI_API_KEY")


# def query_documents(query, id, k=5, model_name="gpt-4o-mini"):
#     """벡터스토어를 기반으로 문서 쿼리"""

#     # 리트리버와 LLM 설정
#     retriever = get_retriever(id, k)

#     # 임베딩 모델 설정
#     llm = ChatOpenAI(model=model_name, temperature=0, api_key=openai_api_key)

#     # 프롬프트 템플릿 정의
#     template = (
#         "다음 정보를 바탕으로 질문에 답변해주세요:\n\n{context}\n\n질문: {question}"
#     )
#     prompt = ChatPromptTemplate.from_template(template)

#     # 문서 형식 변환 함수 정의
#     # def format_docs(docs):
#     #     return "\n\n".join(doc.page_content for doc in docs)

#     # LCEL 체인 구성
#     chain = (
#         {"context": retriever, "question": RunnablePassthrough()}
#         | prompt
#         | llm
#         | StrOutputParser()
#     )

#     # 검색 결과 포함 실행
#     docs = retriever.get_relevant_documents(query)
#     result = chain.invoke(query)

#     response = {"result": result, "source_documents": docs}

#     # API 용도로 사용할 때는 형식화된 결과를 반환
#     # return format_response_for_api(response)

#     return response


# result = query_documents("배기필터의 세척 주기", 36)
# print(result)


################################################################################################################

from vector_db import get_retriever
import os

openai_api_key = os.getenv("OPENAI_API_KEY")


def query_documents(query, id, k=5, model_name="gpt-4o-mini"):
    """벡터스토어를 기반으로 문서 쿼리"""

    # 리트리버 설정
    print(f"DEBUG: 리트리버 설정 - {id}, {k}")
    retriever = get_retriever(id, k)

    print(f"DEBUG: 리트리버 설정 완료 - {retriever}")

    # 검색 결과만 반환
    print(f"DEBUG: 검색 결과 반환 - {query}")
    docs = retriever.invoke(query)
    # print(f"DEBUG: 검색 결과 반환 완료 - {docs}")
    return docs


result = query_documents("배기필터 조립방법", 1)
print(result)


################################################################################################################


# from langchain.docstore.document import Document
# from langchain.text_splitter import RecursiveCharacterTextSplitter
# from langchain_openai import OpenAIEmbeddings
# from langchain_community.vectorstores import FAISS
# import os
# import logging
# from dotenv import load_dotenv

# # 로깅 설정
# logging.basicConfig(level=logging.INFO)
# logger = logging.getLogger(__name__)

# # .env 파일 로드
# load_dotenv()

# # 환경 변수 검증
# openai_api_key = os.getenv("OPENAI_API_KEY")
# if not openai_api_key:
#     logger.warning(
#         "OPENAI_API_KEY 환경 변수가 설정되지 않았습니다. .env 파일을 확인해주세요."
#     )
#     # 개발 환경에서는 기본값을 사용할 수 있도록 수정
#     openai_api_key = "your-api-key-here"  # 실제 API 키로 교체 필요

# # 텍스트 데이터 정의
# text = """
# Open Source

# 정의: 오픈 소스는 소스 코드가 공개되어 누구나 자유롭게 사용, 수정, 배포할 수 있는
# 소프트웨어를 의미합니다. 이는 협업과 혁신을 촉진하는 데 중요한 역할을 합니다.
# 예시: 리눅스 운영 체제는 대표적인 오픈 소스 프로젝트입니다.
# 연관키워드: 소프트웨어 개발, 커뮤니티, 기술 협업

# Structured Data

# 정의: 구조화된 데이터는 정해진 형식이나 스키마에 따라 조직된 데이터입니다.
# 이는 데이터베이스, 스프레드시트 등에서 쉽게 검색하고 분석할 수 있습니다.
# 예시: 관계형 데이터베이스에 저장된 고객 정보 테이블은 구조화된 데이터의 예입니다.
# 연관키워드: 데이터베이스, 데이터 분석, 데이터 모델링

# Parser

# 정의: 파서는 주어진 데이터(문자열, 파일 등)를 분석하여 구조화된 형태로 변환하는 도구입니다.
# 이는 프로그래밍 언어의 구문 분석이나 파일 데이터 처리에 사용됩니다.
# 예시: HTML 문서를 구문 분석하여 웹 페이지의 DOM 구조를 생성하는 것은 파싱의 한 예입니다.
# 연관키워드: 구문 분석, 컴파일러, 데이터 처리

# TF-IDF (Term Frequency-Inverse Document Frequency)

# 정의: TF-IDF는 문서 내에서 단어의 중요도를 평가하는 데 사용되는 통계적 척도입니다.
# 이는 문서 내 단어의 빈도와 전체 문서 집합에서 그 단어의 희소성을 고려합니다.
# 예시: 많은 문서에서 자주 등장하지 않는 단어는 높은 TF-IDF 값을 가집니다.
# 연관키워드: 자연어 처리, 정보 검색, 데이터 마이닝

# Deep Learning

# 정의: 딥러닝은 인공신경망을 이용하여 복잡한 문제를 해결하는 머신러닝의 한 분야입니다.
# 이는 데이터에서 고수준의 표현을 학습하는 데 중점을 둡니다.
# 예시: 이미지 인식, 음성 인식, 자연어 처리 등에서 딥러닝 모델이 활용됩니다.
# 연관키워드: 인공신경망, 머신러닝, 데이터 분석
# """

# try:
#     # 문서 처리
#     doc = Document(page_content=text)
#     text_splitter = RecursiveCharacterTextSplitter(
#         chunk_size=500, chunk_overlap=50  # 더 큰 청크 크기로 변경  # 청크 간 중복 추가
#     )
#     split_docs = text_splitter.split_documents([doc])

#     logger.info(f"문서가 {len(split_docs)}개의 청크로 분할되었습니다.")

#     # 임베딩 및 벡터 데이터베이스 생성
#     embeddings = OpenAIEmbeddings(
#         model="text-embedding-3-small", api_key=openai_api_key
#     )
#     db = FAISS.from_documents(split_docs, embeddings)
#     retriever = db.as_retriever(search_kwargs={"k": 1})

#     print("-" * 200)
#     print(retriever)

#     print("-" * 200)
#     logger.info("벡터 데이터베이스와 리트리버가 성공적으로 생성되었습니다.")

#     # 검색 예시
#     query = "오픈 소스의 정의는 무엇입니까?"
#     docs = retriever.invoke(query)
#     print("-" * 200)
#     print(docs)


# except Exception as e:
#     logger.error(f"오류 발생: {str(e)}")
#     raise
