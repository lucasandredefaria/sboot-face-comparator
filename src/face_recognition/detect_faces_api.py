import sys
import os
import face_recognition
import json

def compare_faces(image_path):
    if not os.path.exists(image_path):
        return json.dumps({"status": "error", "message": f"Imagem '{image_path}' não encontrada."})

    if not image_path.lower().endswith(('.png', '.jpg', '.jpeg', '.bmp')):
        return json.dumps({"status": "error", "message": "O arquivo não é uma imagem válida."})

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
        print("Erro: Forneça o caminho da imagem como parâmetro.")
        sys.exit(1)

    image_path = sys.argv[1]
    result = compare_faces(image_path)
    print(result)

if __name__ == "__main__":
    main()
