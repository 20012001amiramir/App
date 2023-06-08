import android.content.Context

// Входной тип данных в модель [n, 224, 224, 3]
typealias InputType = Array<FloatArray>
typealias OutputType = Array<FloatArray?>

class DiseaseClassifier(
    context: Context
) {

//    private val width = 224
//    private val height = 224
//    private val modelPath = "model.tflite"
//    private val model = Interpreter(
//        getModelByteBuffer(
//            context.assets,
//            modelPath = modelPath
//        ),
//        Interpreter.Options()
//    )

//    fun classify(bitmap: Bitmap): Map<String, Int>? {
//        val pixels = convertBitmapToPixels(bitmap)
//        val input = prepareData(pixels)
//        val output: OutputType = Array(1) {
//            return@Array FloatArray(7) {
//                return@FloatArray 0f
//            }
//        }
//        model.run(input, output)
//
//        // Преобразуем вывод из нейросети в нормальный вид
//        if (output.isNotEmpty()) {
//            return output[0]?.mapIndexed { index, it ->
//                getTypeOfDisease(index) to (it * 100).toInt()
//            }?.filter { it.second != 0 }?.toMap()
//        }
//
//        return null
//    }
//
//    /**
//     * Загружает модель из ассетов приложения, сохраняет и возвращает файл модели
//     *
//     * Падает в out of memory, т.к. файл модели > 300 мб, а под приложение
//     * в ведройде(13) максимум выделяется 256мб
//     */
//    private fun loadModelFile(context: Context): File {
//        val inputStream = context.assets.open(modelPath)
//        val readBytes = inputStream.readBytes()
//        val file = File(context.filesDir, modelPath)
//        if (!file.exists()) {
//            file.writeBytes(readBytes)
//        }
//        return file
//    }
//
//    private fun getModelByteBuffer(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
//        val fileDescriptor = assetManager.openFd(modelPath)
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = fileDescriptor.startOffset
//        val declaredLength = fileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }
//
//    /**
//     * Преобразование изображения в понятный для модели вид
//     */
//    private fun prepareData(pixels: Array<IntArray>): InputType {
//        // Создание массива для входных данных модели
//        val input = Array(height) { FloatArray(width * 3) }
//
//        // Итерирование по всем пикселям и добавление их значений каналов цвета в массив входных данных
//        for (y in 0 until height) {
//            for (x in 0 until width) {
//                val pixel = pixels[y][x]
//
//                // Получение значений каналов цвета
//                val r = (pixel shr 16) and 0xFF
//                val g = (pixel shr 8) and 0xFF
//                val b = pixel and 0xFF
//
//                // Добавление значений каналов цвета в массив входных данных модели
//                input[y][x * 3] = r.toFloat() / 255.0f
//                input[y][x * 3 + 1] = g.toFloat() / 255.0f
//                input[y][x * 3 + 2] = b.toFloat() / 255.0f
//            }
//        }
//
//        return input
//    }
//
//    fun getTypeOfDisease(index: Int): String {
//        val map = mapOf(
//            0 to "Brown rust",
//            1 to "Heathy",
//            2 to "Septorios",
//            3 to "Yellow rust",
//            // 4 to "SAD",
//            // 5 to "SURPRISE",
//            // 6 to "NEUTRAL"
//        )
//        return map[index] ?: "some error"
//    }
//
//    private fun convertBitmapToPixels(bitmap: Bitmap): Array<IntArray> {
//        // Получаем ширину и высоту изображения
//        val width = bitmap.width
//        val height = bitmap.height
//
//        // Создаем массив пикселей
//        val pixels = Array(height) { IntArray(width) }
//
//        // Итерируем по всем пикселям изображения и заполняем массив пикселей
//        for (x in 0 until width) {
//            for (y in 0 until height) {
//                pixels[y][x] = bitmap.getPixel(x, y)
//            }
//        }
//
//        return pixels
//    }


}