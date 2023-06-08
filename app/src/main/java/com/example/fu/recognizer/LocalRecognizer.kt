package com.example.fu.recognizer

//
//class LocalRecognizer @Inject constructor(
//    private var context: Context,
//    private var classifier: DiseaseClassifier
//) : Recognizer {
////
////    override fun recognize(imageUri: Uri): Observable {
////        return android.database.Observable. { emitter: android.database.Observable<Bitmap> ->
////            val target = object : SimpleTarget<Bitmap>() {
////                override fun onResourceReady(@NonNull resource: Bitmap, transition: Transition<in Bitmap>?) {
////                    emitter.onNext(resource)
////                    emitter.onComplete()
////                }
////
////                override fun onLoadFailed(errorDrawable: Drawable?) {
////                    emitter.onError(Throwable("Failed to load image"))
////                }
////            }
////
////            Glide.with(context)
////                .asBitmap()
////                .load(imageUri)
////                .into(target)
////        }
////    }
//
//}