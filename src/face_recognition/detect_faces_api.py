import sys
import os
import face_recognition
import json

def count_faces(image_path):
    if not os.path.exists(image_path):
        return json.dumps({"status": "error", "message": f"Imagem {image_path} nao encontrada."})

    if not image_path.lower().endswith(('.png', '.jpg', '.jpeg', '.bmp')):
        return json.dumps({"status": "error", "message": "O arquivo nao e uma imagem valida."})

    try:
        image = face_recognition.load_image_file(image_path)
        face_locations = face_recognition.face_locations(image)

        return json.dumps({
            "status": "success",
            "face_count": len(face_locations),
            "face_locations": face_locations
        })
    except Exception as e:
        return json.dumps({"status": "error", "message": f"Erro ao processar a imagem: {str(e)}"})

def main():
    if len(sys.argv) != 2:
        print("Erro: Forneca o caminho da imagem como parametro.")
        sys.exit(1)

    image_path = sys.argv[1]
    print(image_path)
    result = count_faces(image_path)
    print(result)

if __name__ == "__main__":
    main()