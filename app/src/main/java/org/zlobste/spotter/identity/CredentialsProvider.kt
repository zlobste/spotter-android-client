package org.zlobste.spotter.identity

import org.zlobste.spotter.features.sign_in.model.Credentials

interface CredentialsProvider {
    fun setCredentials(credentials: Credentials?)
    fun getCredentials(): Credentials?
}