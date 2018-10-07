package com.example.xiewujie.musicdemo.module

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.StringReader
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.util.regex.Pattern
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class CallbackToJsonConvertFactory private constructor(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                       retrofit: Retrofit): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CallBackToJsonConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type,
                                      parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyConverter(gson, adapter)
    }

    companion object {

        fun create(): CallbackToJsonConvertFactory {
            return create(Gson())
        }


        fun create(gson: Gson): CallbackToJsonConvertFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return CallbackToJsonConvertFactory(gson)
        }
    }
}

    internal class CallBackToJsonConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T {

            var content = value.string()
//            val startIndex = content.indexOf("(")
//            val otherStartIndex = content.indexOf("{")
//            val endIndex = content.indexOfLast { ')'== it }
//            val e = content.indexOfLast { it == '}' }
//
//            if (otherStartIndex-startIndex>0&&endIndex>e&&startIndex != -1){
//                //content = content.substring(startIndex+1,endIndex).toString()
//                Log.d("start",content)
//            }
            content = findContent(content)
            val jsonReader = gson.newJsonReader(StringReader(content))
            try {
                return adapter.read(jsonReader)
            } finally {
                value.close()
            }
        }

        fun findContent(str:String):String{
            val regex = "[^{]*(.*)"
          //  val regex = "(//(.*//))"
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(str)
            if (matcher.find()){
                return matcher.group(1)
            }else{
                return str
            }
        }
    }

internal class GsonRequestBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<T, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
    }

    companion object {
        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
        private val UTF_8 = Charset.forName("UTF-8")
    }
}