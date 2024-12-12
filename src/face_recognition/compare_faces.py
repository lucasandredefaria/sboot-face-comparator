import sys
import face_recognition

def compare_faces(image_path1, image_path2):
    # Carregar as imagens
    image1 = face_recognition.load_image_file(image_path1)
    image2 = face_recognition.load_image_file(image_path2)

    # Obter os encodings das faces
    face_encoding1 = face_recognition.face_encodings(image1)
    face_encoding2 = face_recognition.face_encodings(image2)

    # Verificar se as imagens contêm faces
    if not face_encoding1 or not face_encoding2:
        return "Uma ou ambas as imagens não contêm faces."

    # Comparar as faces usando os encodings
    result = face_recognition.compare_faces([face_encoding1[0]], face_encoding2[0])

    if result[0]:
        return "As faces são iguais!"
    else:
        return "As faces são diferentes."

if __name__ == "__main__":
    # Pega os argumentos de linha de comando
    image_path1 = sys.argv[1]
    image_path2 = sys.argv[2]

    # Chama a função de comparação e imprime o resultado
    result = compare_faces(image_path1, image_path2)
    print(result)