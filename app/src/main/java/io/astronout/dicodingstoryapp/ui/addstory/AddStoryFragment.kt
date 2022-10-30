package io.astronout.dicodingstoryapp.ui.addstory

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.viewbinding.library.fragment.viewBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.astronout.dicodingstoryapp.R
import io.astronout.dicodingstoryapp.databinding.FragmentAddStoryBinding
import io.astronout.dicodingstoryapp.ui.base.BaseFragment
import io.astronout.dicodingstoryapp.utils.*
import io.astronout.dicodingstoryapp.vo.Resource
import java.io.File
import java.io.FileOutputStream

class AddStoryFragment : BaseFragment(R.layout.fragment_add_story), AddStoryContract {

    private val binding: FragmentAddStoryBinding by viewBinding()
    private val viewModel: AddStoryViewModel by viewModels()
    private val navController: NavController? by lazy { findNavController() }

    private var file: File? = null
    private lateinit var currentPhotoPath: String

    override fun initUI() {
        super.initUI()
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initAction() {
        super.initAction()
        with(binding) {
            btnCamera.setOnClickListener {
                onOpenCamera()
            }
            btnGallery.setOnClickListener {
                onOpenGallery()
            }
            btnCreateStory.setOnClickListener {
                when {
                    file == null -> showToast(getString(R.string.error_empty_image))
                    etDescription.text.toString().isEmpty() -> etDescription.error = getString(R.string.error_empty_description)
                    else -> {
                        uploadStory()
                    }
                }
            }
        }
    }

    override fun onOpenGallery() {
        Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
            galleryLauncher.launch(Intent.createChooser(this, getString(R.string.label_select_image)))
        }
    }

    override fun onOpenCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            resolveActivity(requireActivity().packageManager)
            createNewTempFile(requireContext()).let {
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "io.astronout.dicodingstoryapp",
                    it
                )
                currentPhotoPath = it.absolutePath
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                cameraLauncher.launch(this)
            }
        }
    }

    override fun uploadStory() {
        file?.let { file ->
            collectLifecycleFlow(viewModel.addNewStory(file.compress(), binding.etDescription.text.toString())) {
                when (it) {
                    is Resource.Error -> {
                        progress.dismiss()
                        showToast(it.message)
                    }
                    is Resource.Loading -> progress.show()
                    is Resource.Success -> {
                        progress.dismiss()
                        showToast(getString(R.string.label_story_uploaded))
                        navController?.navigateUp()
                    }
                }
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            result.data?.data?.let {
                file = it.toFile(requireContext())
                binding.ivStoryImage.setImageURI(it)
            }
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            file = File(currentPhotoPath)
            val rotatedBitmap = BitmapFactory.decodeFile(file?.path).rotate(currentPhotoPath)
            runCatching {
                val os = FileOutputStream(file)
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.flush()
                os.close()
            }

            binding.ivStoryImage.setImageBitmap(rotatedBitmap)
        }
    }

}