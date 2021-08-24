package com.Towrevo.aws

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.Towrevo.util.S3BUCKET_NAME
import com.Towrevo.util.S3POOL_ID
import id.zelory.compressor.Compressor
import io.reactivex.schedulers.Schedulers
import java.io.File


class AmazoneUploadUtil(
    var context: Context,
    var filelist: MutableList<File>,
    var s3Client: AmazonS3Client?=null,
    var awsAmazoneUploadUtil: AwsUploadCompleteListner
) {

    private var currentUploadIndex = 0
    private var multipleImageName: String = ""
    private var imagename: String = ""
    private lateinit var transferUtility: TransferUtility


    fun initialize() {

        uploadFile()

    }

    private fun singleFileUpload(file: File) {

        s3credentialsProvider()

        // callback method to call the setTransferUtility method
        setTransferUtility()
        val transferObserver = transferUtility.upload(
            S3BUCKET_NAME, /* The bucket to upload to */
            imagename, /* The key for the uploaded object */
            file /* The file where the data to upload exists */
        )

        transferObserverListener(transferObserver)


    }

    fun transferObserverListener(transferObserver: TransferObserver) {

        transferObserver.setTransferListener(object : TransferListener {

            override fun onStateChanged(id: Int, state: TransferState) {

                if(state==TransferState.COMPLETED)
                {
                    currentUploadIndex++
                    uploadFile()
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val percentage = (bytesCurrent / bytesTotal * 100).toInt()

            }

            override fun onError(id: Int, ex: Exception) {
                uploadFile()
            }

        })
    }
    fun s3credentialsProvider() {

        // Initialize the AWS Credential
        val cognitoCachingCredentialsProvider = CognitoCachingCredentialsProvider(
            context, S3POOL_ID, // Identity Pool ID
            Regions.US_EAST_1 // Region
        )
        createAmazonS3Client(cognitoCachingCredentialsProvider)
    }

    fun createAmazonS3Client(credentialsProvider: CognitoCachingCredentialsProvider) {

        // Create an S3 client
        s3Client = AmazonS3Client(credentialsProvider)

        // Set the region of your S3 bucket
        (s3Client as AmazonS3Client).setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_EAST_1))
    }

    fun setTransferUtility() {

        transferUtility = TransferUtility(s3Client, context)
    }


    private fun uploadFile() {
        if (filelist.size > currentUploadIndex) {
            compressImage()
        } else {
            awsAmazoneUploadUtil.onComplete(multipleImageName)
        }
    }


    private fun compressImage() {

        val subscribe = Compressor(context)
            .compressToFileAsFlowable(filelist.get(currentUploadIndex))
            .subscribeOn(Schedulers.io())
            .subscribe({ file ->
                imagename = file.name

                if (filelist.size - 1 != currentUploadIndex && filelist.size != 1)
                    multipleImageName = multipleImageName + file.name + ","
                else
                    multipleImageName += file.name

                singleFileUpload(file)

            }, { throwable ->
                throwable.printStackTrace()

            })
    }

    interface AwsUploadCompleteListner {
        fun onComplete(imageName: String = "")
    }
}