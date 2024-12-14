import os
import shutil
import face_recognition
import pytesseract
from PIL import Image
import json

def process_images_in_directory(directory_path):
    # Verificar se o diretório existe
    if not os.path.exists(directory_path):
        return json.dumps({"status": "error", "message": f"Diretório {directory_path} não encontrado."})

    # Lista todas as imagens no diretório
    image_files = [f for f in os.listdir(directory_path) if f.lower().endswith(('.png', '.jpg', '.jpeg'))]

    face_encodings = []
    face_groups = []
    images_with_text = []
    text_from_images = {}

    # Processamento das imagens
    for image_file in image_files:
        image_path = os.path.join(directory_path, image_file)

        try:
            # Carregar a imagem para OCR (detecção de texto)
            image = Image.open(image_path)
            text = pytesseract.image_to_string(image)
            if text.strip():  # Se houver texto detectado
                images_with_text.append(image_file)
                text_from_images[image_file] = text.strip()

            # Carregar a imagem para detecção de faces
            image_data = face_recognition.load_image_file(image_path)
            encodings = face_recognition.face_encodings(image_data)

            if encodings:
                match_found = False
                for idx, known_encoding in enumerate(face_encodings):
                    # Verifica se a face na imagem atual é igual a alguma face conhecida
                    matches = face_recognition.compare_faces([known_encoding], encodings[0])
                    if True in matches:
                        face_groups[idx].append(image_file)
                        match_found = True
                        break

                if not match_found:
                    face_encodings.append(encodings[0])
                    face_groups.append([image_file])

        except Exception as e:
            return json.dumps({"status": "error", "message": f"Erro ao processar a imagem {image_file}: {str(e)}"})

    # Organiza as imagens nas pastas com base no texto detectado
    output_dir = os.path.join(directory_path, "organized_images")
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    for image_file in images_with_text:
        text = text_from_images[image_file]
        # Cria uma pasta para cada texto encontrado
        text_folder = os.path.join(output_dir, text)
        if not os.path.exists(text_folder):
            os.makedirs(text_folder)

        # Move a imagem para a pasta correspondente
        src_path = os.path.join(directory_path, image_file)
        dst_path = os.path.join(text_folder, image_file)
        shutil.move(src_path, dst_path)

    # Comparar as faces nas imagens com texto com outras imagens e movê-las para as pastas de texto
    for group in face_groups:
        for image_file in group:
            for image_with_text in images_with_text:
                text = text_from_images[image_with_text]
                src_path = os.path.join(directory_path, image_file)
                dst_folder = os.path.join(output_dir, text)
                if not os.path.exists(dst_folder):
                    os.makedirs(dst_folder)

                dst_path = os.path.join(dst_folder, image_file)
                shutil.move(src_path, dst_path)

    return json.dumps({"status": "success", "message": "Imagens processadas e organizadas com sucesso!"})

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 2:
        print("Erro: Forneça o caminho do diretório como parâmetro.")
        sys.exit(1)

    directory_path = sys.argv[1]
    result = process_images_in_directory(directory_path)
    print(result)
