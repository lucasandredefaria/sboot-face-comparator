import os
import shutil
import face_recognition
import pytesseract
from PIL import Image
import json
import logging

# Configuracao do logger
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def process_image_for_text(image_path):
    """Processa a imagem para detectar texto usando OCR."""
    try:
        image = Image.open(image_path)
        text = pytesseract.image_to_string(image).strip()
        return text
    except Exception as e:
        logging.error(f"Erro ao processar OCR para a imagem {image_path}: {e}")
        return ""

def process_image_for_faces(image_path, face_encodings, face_groups):
    """Detecta faces na imagem e as compara com faces conhecidas."""
    try:
        image_data = face_recognition.load_image_file(image_path)
        encodings = face_recognition.face_encodings(image_data)

        if encodings:
            match_found = False
            for idx, known_encoding in enumerate(face_encodings):
                matches = face_recognition.compare_faces([known_encoding], encodings[0])
                if True in matches:
                    face_groups[idx].append(image_path)
                    match_found = True
                    break

            if not match_found:
                face_encodings.append(encodings[0])
                face_groups.append([image_path])

        return face_encodings, face_groups
    except Exception as e:
        logging.error(f"Erro ao processar deteccao de face para a imagem {image_path}: {e}")
        return face_encodings, face_groups

def move_image_to_folder(image_path, text, output_dir):
    """Move a imagem para a pasta correspondente ao texto detectado."""
    text_folder = os.path.join(output_dir, text)
    os.makedirs(text_folder, exist_ok=True)

    dst_path = os.path.join(text_folder, os.path.basename(image_path))
    shutil.move(image_path, dst_path)
    logging.info(f"Imagem {os.path.basename(image_path)} movida para {text_folder}")

def process_images_in_directory(directory_path):
    """Processa as imagens de um diretorio e organiza conforme o texto e faces detectadas."""
    if not os.path.exists(directory_path):
        return json.dumps({"status": "error", "message": f"Diretorio {directory_path} nao encontrado."})

    image_files = [f for f in os.listdir(directory_path) if f.lower().endswith(('.png', '.jpg', '.jpeg'))]
    logging.info(f"Imagens encontradas: {image_files}")

    face_encodings = []
    face_groups = []
    images_with_text = []
    text_from_images = {}

    # Processamento das imagens
    for image_file in image_files:
        image_path = os.path.join(directory_path, image_file)

        # Detecta texto na imagem
        text = process_image_for_text(image_path)
        if text:
            images_with_text.append(image_file)
            text_from_images[image_file] = text

        # Detecta faces na imagem
        face_encodings, face_groups = process_image_for_faces(image_path, face_encodings, face_groups)

    # Organiza as imagens nas pastas com base no texto detectado
    output_dir = os.path.join(directory_path, "organized_images")
    os.makedirs(output_dir, exist_ok=True)

    # Move imagens para pastas com base no texto detectado
    for image_file in images_with_text:
        text = text_from_images[image_file]
        move_image_to_folder(os.path.join(directory_path, image_file), text, output_dir)

    # Comparar as faces nas imagens com texto e mover as imagens para as pastas de texto
    for group in face_groups:
        for image_file in group:
            for image_with_text in images_with_text:
                text = text_from_images[image_with_text]
                move_image_to_folder(os.path.join(directory_path, image_file), text, output_dir)

    return json.dumps({"status": "success", "message": "Imagens processadas e organizadas com sucesso!"})

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 2:
        logging.error("Erro: Forneca o caminho do diretorio como parametro.")
        sys.exit(1)

    directory_path = sys.argv[1]
    result = process_images_in_directory(directory_path)
    logging.info(result)
