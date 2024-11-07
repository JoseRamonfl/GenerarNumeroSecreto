import java.io.File        // Importa la clase File para trabajar con archivos
import kotlin.random.Random // Importa la clase Random para generar números aleatorios

fun main() {
    val secreto = adivinaNumero() // Genera un número aleatorio de 4 dígitos sin repetición
    val maxIntentos = 10          // Define el número máximo de intentos que el usuario tiene para adivinar
    var intentos = 0              // Inicializa el contador de intentos en 0
    var adivinado = false         // Variable que indica si el número ha sido adivinado

    // Cargar la última jugada desde el archivo
    val archivo = File("ultima_jugada.txt") // Crea un objeto File para leer/escribir el archivo
    if (archivo.exists()) {                 // Verifica si el archivo existe
        println("Última jugada: ${archivo.readText()}") // Lee y muestra el contenido del archivo
    }

    // Bucle principal de juego: se repite hasta que el usuario adivine o alcance el máximo de intentos
    while (intentos < maxIntentos && !adivinado) {
        println("Intento ${intentos + 1}/$maxIntentos: Introduce un número de 4 cifras del 1 al 6 sin repeticiones.")
        val intento = readLine()?.trim() // Lee el número introducido por el usuario y elimina espacios en blanco

        if (validarIntento(intento)) { // Verifica si el intento cumple las reglas
            // Llama a la función para contar aciertos y coincidencias
            val (aciertos, coincidencias) = compararNumeros(secreto, intento!!)
            // Muestra los resultados del intento usando colores ANSI
            println("\u001B[32mAciertos: $aciertos\u001B[0m \u001B[33mCoincidencias: $coincidencias\u001B[0m")

            if (aciertos == 4) { // Si el número tiene 4 aciertos, el usuario ha adivinado
                println("¡Felicidades! Has adivinado el número.")
                adivinado = true // Cambia a true para salir del bucle
            } else {
                intentos++ // Incrementa el contador de intentos
            }
        } else {
            println("Número inválido. Asegúrate de que tiene 4 cifras distintas entre 1 y 6.") // Mensaje de error si el número es inválido
        }
    }

    if (!adivinado) { // Si no se adivinó el número después de todos los intentos
        println("Lo siento, no adivinaste el número. Era $secreto.") // Muestra el número secreto
    }

    // Guardar la última jugada en el archivo para futura referencia
    archivo.writeText("Intentos: $intentos, Número secreto: $secreto") // Escribe los datos de la última jugada en el archivo
}

// Función para generar un número secreto aleatorio de 4 dígitos sin repeticiones
fun adivinaNumero(): String {
    val digitos = (1..6).shuffled().take(4) // Genera una lista de 4 dígitos aleatorios del 1 al 6 sin repeticiones
    return digitos.joinToString("")          // Convierte la lista de dígitos en un String y lo retorna
}

// Función para validar que el intento ingresado cumple con las reglas
fun validarIntento(intento: String?): Boolean {
    // Verifica que el intento no sea nulo, tenga 4 caracteres, use solo los dígitos del 1 al 6 y no tenga cifras repetidas
    return intento != null && intento.length == 4 &&
            intento.all { it in '1'..'6' } && intento.toSet().size == 4
}

// Función para contar los aciertos y coincidencias entre el número secreto y el intento
fun compararNumeros(secreto: String, intento: String): Pair<Int, Int> {
    var aciertos = 0 // Contador de aciertos
    var coincidencias = 0 // Contador de coincidencias

    // Recorre cada posición en el intento y la compara con el número secreto
    for (i in 0..3) {
        if (intento[i] == secreto[i]) aciertos++ // Incrementa aciertos si la cifra y posición coinciden
        else if (intento[i] in secreto) coincidencias++ // Incrementa coincidencias si la cifra está en el secreto pero en diferente posición
    }

    return Pair(aciertos, coincidencias) // Retorna el número de aciertos y coincidencias como un par de valores
}
