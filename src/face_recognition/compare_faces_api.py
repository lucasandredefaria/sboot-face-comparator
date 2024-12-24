import sys
import os
import face_recognition
import json

def compare_faces(image_path1, image_path2):
    # Verificar se os arquivos existem
    if not os.path.exists(image_path1):
        return json.dumps({"status": "error", "message": f"Imagem {image_path1} nao encontrada."})
    if not os.path.exists(image_path2):
        return json.dumps({"status": "error", "message": f"Imagem {image_path2} nao encontrada."})

    try:
        # Carregar as imagens
        image1 = face_recognition.load_image_file(image_path1)
        image2 = face_recognition.load_image_file(image_path2)

        # Obter os encodings das faces
        face_encoding1 = face_recognition.face_encodings(image1)
        face_encoding2 = face_recognition.face_encodings(image2)

        # Verificar se as imagens contêm faces
        if not face_encoding1 or not face_encoding2:
            return json.dumps({"status": "error", "message": "Uma ou ambas as imagens nao contem faces."})

        # Comparar as faces
        result = face_recognition.compare_faces([face_encoding1[0]], face_encoding2[0])

        # Retornar o resultado
        return json.dumps({"status": "success", "result": "As faces sao iguais!" if result[0] else "As faces sao diferentes."})

    except Exception as e:
        return json.dumps({"status": "error", "message": f"Erro ao processar as imagens: {str(e)}"})

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Erro: Forneça dois caminhos de imagem como parâmetros.")
        sys.exit(1)

    image_path1 = sys.argv[1]
    image_path2 = sys.argv[2]

    result = compare_faces(image_path1, image_path2)
    print(result)