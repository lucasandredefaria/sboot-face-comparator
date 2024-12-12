from flask import Flask, request, jsonify
import face_recognition
import numpy as np
import io
from PIL import Image

app = Flask(__name__)

# Função para calcular a similaridade entre duas imagens
def compare_faces(image1, image2):
    # Carregar as imagens em formato de bytes
    img1 = face_recognition.load_image_file(image1)
    img2 = face_recognition.load_image_file(image2)

    # Obter as faces presentes nas imagens
    face_encodings1 = face_recognition.face_encodings(img1)
    face_encodings2 = face_recognition.face_encodings(img2)

    if len(face_encodings1) == 0 or len(face_encodings2) == 0:
        return -1  # Retorna -1 se não houver faces detectadas em uma das imagens

    # Comparar as faces
    results = face_recognition.compare_faces([face_encodings1[0]], face_encodings2[0])
    
    # Retorna 1 se as faces forem iguais, 0 caso contrário
    return 1 if results[0] else 0

# Endpoint para comparar duas imagens
@app.route('/compare-faces', methods=['POST'])
def compare_faces_endpoint():
    try:
        # Receber as imagens como arquivos
        file1 = request.files['image1']
        file2 = request.files['image2']
        
        # Salvar as imagens temporariamente
        img1 = io.BytesIO(file1.read())
        img2 = io.BytesIO(file2.read())

        # Comparar as imagens
        similarity = compare_faces(img1, img2)

        # Retornar o resultado da comparação
        return jsonify({'similarity': similarity})

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
